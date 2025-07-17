package com.code_room.interview_service.domain.usecase;

import com.code_room.interview_service.domain.enums.InterviewStatus;
import com.code_room.interview_service.domain.mapper.InterviewMapper;
import com.code_room.interview_service.domain.model.Interview;
import com.code_room.interview_service.domain.model.enums.NotificationType;
import com.code_room.interview_service.domain.ports.EncryptService;
import com.code_room.interview_service.domain.ports.InterviewService;
import com.code_room.interview_service.infrastructure.Config.EncryptionProperties;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewDto;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewEventDto;
import com.code_room.interview_service.infrastructure.messaging.FcmMessage;
import com.code_room.interview_service.infrastructure.messaging.KafkaProducer;
import com.code_room.interview_service.infrastructure.repository.InterviewRepository;
import com.code_room.interview_service.infrastructure.restclient.OfferApiService;
import com.code_room.interview_service.infrastructure.restclient.dto.LangageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    InterviewMapper interviewMapper;
    @Autowired
    InterviewRepository interviewRepository;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    EncryptService encryptService;
    @Autowired
    EncryptionProperties encryptionProperties;
    @Autowired
    OfferApiService offerApiService;


    @Override
    public void handleAcceptedApplicants(InterviewEventDto event) {
        System.out.println("description : " + event.getDescription());
        List<String> allUsers = event.getAcceptedUsers();
        int groupSize = 4;
        int total = allUsers.size();

        for (int i = 0; i < total; i += groupSize) {
            List<String> group = allUsers.subList(i, Math.min(i + groupSize, total));

            Interview interview = Interview.builder()
                    .offerId(event.getOfferId())
                    .adminEmail(event.getAdminEmail())
                    .participants(group)
                    .description(event.getDescription())
                    .scheduledAt(null)
                    .link(null)
                    .status(InterviewStatus.NOT_SCHEDULED)
                    .build();

            Interview saved = interviewMapper.toModel(interviewRepository.save(interviewMapper.toEntity(interview)));
            interviewRepository.save(interviewMapper.toEntity(saved));
        }
    }



    private String generateLink(String userEmail, String interviewId, LocalDateTime fechaHoraEntrevista,List<String> acceptedUsers) {
        try {
            String baseUrl = encryptionProperties.getLink();

            String fechaFormateada = fechaHoraEntrevista.format(DateTimeFormatter.ISO_DATE_TIME);

            String query = String.format(
                    "interviewId=%s&email=%s&fecha=%s&participants=%s",
                    URLEncoder.encode(interviewId, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(userEmail, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(fechaFormateada, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(String.join(",", acceptedUsers), StandardCharsets.UTF_8.toString())
            );

            String encrypted = encryptService.encrypt(query);

            return String.format("%s?data=%s", baseUrl, encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Error generando URL firmada: " + e.getMessage(), e);
        }
    }


    private void notificationsClients(List<String> participants,String link, LocalDateTime dateTime,String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);


        FcmMessage message = FcmMessage.builder()
                .to(email)
                .source(NotificationType.INTERVIEW_SCHEDULED)
                .data(Map.of(
                        "date", dateTime.toString(),
                        "link", link
                ))
                .build();

        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaProducer.sendMessage("notification-topic", json);

        } catch (JsonProcessingException e) {
            System.err.println("❌ Error converting FcmMessage to JSON for email " + email + ": " + e.getMessage());
            e.printStackTrace();
        }

    }



    @Override
    public Interview createInterview(Interview interview) {
        return interviewMapper.toModel(interviewRepository.save(interviewMapper.toEntity(interview)));
    }

    @Override
    public InterviewDto updateInterview(String id, InterviewDto interviewDto) {
        Interview existing = interviewRepository.findById(id)
                .map(interviewMapper::toModel)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));

        existing.setParticipants(interviewDto.getParticipants());
        existing.setStatus(interviewDto.getStatus());
        existing.setScheduledAt(interviewDto.getScheduledAt());

        Interview updated = interviewMapper.toModel(interviewRepository.save(interviewMapper.toEntity(existing)));
        return interviewMapper.toDto(updated);
    }

    @Override
    public void deleteInterview(String id) {
        Interview interview = interviewRepository.findById(id)
                .map(interviewMapper::toModel)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));
        interviewRepository.deleteById(interview.getId());
    }
    @Override
    public InterviewDto scheduleInterview(String id, LocalDateTime dateTime) {
        Interview interview = interviewRepository.findById(id)
                .map(interviewMapper::toModel)
                .orElseThrow(() -> new RuntimeException("Entrevista no encontrada con ID: " + id));
        interview.setScheduledAt(dateTime);
        interview.setStatus(InterviewStatus.SCHEDULED);

        for (String userEmail : interview.getParticipants()) {
            String link = generateLink(userEmail, id, dateTime, interview.getParticipants());
            notificationsClients(interview.getParticipants(), link,dateTime,userEmail);
        }

        String representativeLink = generateLink(interview.getAdminEmail(), id, dateTime, interview.getParticipants());
        interview.setLink(representativeLink);
        notificationsClients(interview.getParticipants(),representativeLink,dateTime,interview.getAdminEmail());
        Interview interviewModel = interviewMapper.toModel(interviewRepository.save(interviewMapper.toEntity(interview)));
        return interviewMapper.toDto(interviewModel);
    }

    @Override
    public List<InterviewDto> getInterviewsByOfferId(String offerId) {
        return interviewRepository.findByOfferId(offerId)
                .stream()
                .map(interviewMapper::toModel)
                .map(interviewMapper::toDto)
                .toList();
    }

    @Override
    public Page<InterviewDto> getAllNotScheduled(int page, String email) {
        Pageable pageable = PageRequest.of(page, 6);
        return interviewRepository.findAllByStatusAndAdminEmail(InterviewStatus.NOT_SCHEDULED, email, pageable)
                .map(interviewMapper::toModel)
                .map(interviewMapper::toDto);
    }

    @Override
    public LangageDto getLanguage(String authHeader, String id) {
        try {
            String offerId = interviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("no found interview " + id))
                    .getOfferId();
            Response<LangageDto> response = offerApiService.getLanguage(authHeader, offerId).execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Respuesta vacía";
                throw new RuntimeException("Error HTTP " + response.code() + ": " + errorMsg);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error de red: " + e.getMessage(), e);
        }
    }

    @Override
    public String getAdmin(String id){
        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("no found interview " + id))
                .getAdminEmail();
    }





}
