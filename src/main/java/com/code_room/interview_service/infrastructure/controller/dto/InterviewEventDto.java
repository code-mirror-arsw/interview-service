package com.code_room.interview_service.infrastructure.controller.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEventDto {
    private String offerId;

    private String adminEmail;

    private List<String> acceptedUsers;

    private String description;

}