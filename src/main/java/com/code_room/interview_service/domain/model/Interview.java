package com.code_room.interview_service.domain.model;

import com.code_room.interview_service.domain.enums.InterviewStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    private String id;
    private String offerId;
    private String adminEmail;
    private LocalDateTime scheduledAt;
    private String description;
    private String link;
    private List<String> participants;
    private InterviewStatus status;
}
