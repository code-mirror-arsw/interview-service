package com.code_room.interview_service.domain.mapper;

import com.code_room.interview_service.domain.model.Interview;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewDto;
import com.code_room.interview_service.infrastructure.repository.entities.InterviewEntity;

public interface InterviewMapper {
    InterviewEntity toEntity(Interview interview);

    Interview toModel(InterviewEntity entity);

    InterviewDto toDto(Interview model);

    Interview toModel(InterviewDto dto);
}
