package com.code_room.interview_service.domain.mapper;

import com.code_room.interview_service.domain.model.Interview;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewDto;
import com.code_room.interview_service.infrastructure.repository.entities.InterviewEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapperImpl implements InterviewMapper{

    @Override
    public InterviewEntity toEntity(Interview interview) {
        return InterviewEntity.builder()
                .id(interview.getId())
                .offerId(interview.getOfferId())
                .adminEmail(interview.getAdminEmail())
                .description(interview.getDescription())
                .scheduledAt(interview.getScheduledAt())
                .link(interview.getLink())
                .participants(interview.getParticipants())
                .status(interview.getStatus())
                .build();
    }
    @Override
    public Interview toModel(InterviewEntity entity) {
        return Interview.builder()
                .id(entity.getId())
                .offerId(entity.getOfferId())
                .adminEmail(entity.getAdminEmail())
                .scheduledAt(entity.getScheduledAt())
                .description(entity.getDescription())
                .link(entity.getLink())
                .participants(entity.getParticipants())
                .status(entity.getStatus())
                .build();
    }
    @Override
    public InterviewDto toDto(Interview model) {
        return InterviewDto.builder()
                .id(model.getId())
                .offerId(model.getOfferId())
                .adminEmail(model.getAdminEmail())
                .description(model.getDescription())
                .scheduledAt(model.getScheduledAt())
                .link(model.getLink())
                .participants(model.getParticipants())
                .status(model.getStatus())
                .build();
    }
    @Override
    public Interview toModel(InterviewDto dto) {
        return Interview.builder()
                .offerId(dto.getOfferId())
                .adminEmail(dto.getAdminEmail())
                .scheduledAt(dto.getScheduledAt())
                .description(dto.getDescription())
                .link(dto.getLink())
                .participants(dto.getParticipants())
                .status(dto.getStatus())
                .build();
    }
}
