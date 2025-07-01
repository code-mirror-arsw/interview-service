package com.code_room.interview_service.infrastructure.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewInfoDto {
    private String interviewId;
    private String status;
    private LocalDateTime interviewDate;
}
