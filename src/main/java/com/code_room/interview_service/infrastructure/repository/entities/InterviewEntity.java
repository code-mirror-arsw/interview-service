package com.code_room.interview_service.infrastructure.repository.entities;

import com.code_room.interview_service.domain.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String offerId;

    @Column(nullable = false)
    private String adminEmail;

    private LocalDateTime scheduledAt;

    @Column(length = 1024)
    private String link;

    private String description;


    @ElementCollection
    @CollectionTable(name = "interview_participants", joinColumns = @JoinColumn(name = "interview_id"))
    @Column(name = "user_id")
    private List<String> participants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;
}
