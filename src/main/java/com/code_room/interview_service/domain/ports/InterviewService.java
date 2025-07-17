package com.code_room.interview_service.domain.ports;


import com.code_room.interview_service.domain.model.Interview;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewDto;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewEventDto;
import com.code_room.interview_service.infrastructure.restclient.dto.LangageDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface InterviewService {


    InterviewDto scheduleInterview(String id, LocalDateTime dateTime);

    List<InterviewDto> getInterviewsByOfferId(String offerId);

    Interview createInterview(Interview interview);

    void handleAcceptedApplicants(InterviewEventDto event);

    InterviewDto updateInterview(String id, InterviewDto interviewDto);

    void deleteInterview(String id);

    Page<InterviewDto> getAllNotScheduled(int page, String adminEmail);

    LangageDto getLanguage(String authHeader,String id) throws IOException;

    String getAdmin(String id);
}
