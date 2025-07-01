package com.code_room.interview_service.infrastructure.repository;

import com.code_room.interview_service.domain.enums.InterviewStatus;
import com.code_room.interview_service.infrastructure.repository.entities.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewRepository extends JpaRepository<InterviewEntity,String> {
    List<InterviewEntity> findByOfferId(String offerId);

    @Query("""
       SELECT i
       FROM InterviewEntity i
       WHERE i.status = :status
         AND i.adminEmail = :adminEmail
       """)
    Page<InterviewEntity> findAllByStatusAndAdminEmail(
            @Param("status") InterviewStatus status,
            @Param("adminEmail") String adminEmail,
            Pageable pageable);

}
