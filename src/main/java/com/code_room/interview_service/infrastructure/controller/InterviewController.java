package com.code_room.interview_service.infrastructure.controller;

import com.code_room.interview_service.domain.model.Interview;
import com.code_room.interview_service.domain.ports.InterviewService;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewDto;
import com.code_room.interview_service.infrastructure.repository.entities.InterviewEntity;
import com.code_room.interview_service.infrastructure.restclient.dto.LangageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    @Autowired
    InterviewService interviewService;

    @GetMapping("/by-offer/{offerId}")
    public ResponseEntity<?> getInterviewsByOffer(@PathVariable String offerId) {
        try {
            List<InterviewDto> interviews = interviewService.getInterviewsByOfferId(offerId);
            return ResponseEntity.ok(interviews);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al obtener entrevistas por oferta.");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}/schedule")
    public ResponseEntity<?> scheduleInterview(
            @PathVariable String id,
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            InterviewDto interviewDto = interviewService.scheduleInterview(id, dateTime);
            response.put("code", 200);
            response.put("message", "Interview successfully scheduled.");
            response.put("data", interviewDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 404);
            response.put("message", "Interview scheduling failed.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Internal server error.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInterview(@PathVariable String id, @RequestBody InterviewDto interviewDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            InterviewDto updated = interviewService.updateInterview(id, interviewDto);
            response.put("code", 200);
            response.put("message", "Interview updated successfully.");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 404);
            response.put("message", "Interview update failed.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Internal server error.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInterview(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            interviewService.deleteInterview(id);
            response.put("code", 200);
            response.put("message", "Interview deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 404);
            response.put("message", "Interview deletion failed.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Internal server error.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/adminEmail/{adminEmail}")
    public ResponseEntity<?> getAllNotScheduled(@RequestParam(defaultValue = "0") int page,
                                                @PathVariable String adminEmail) {
        try {
            Page<InterviewDto> interview = interviewService.getAllNotScheduled(page,adminEmail);
            return ResponseEntity.ok(interview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message" , e.getMessage()));
        }
    }

    @GetMapping("/language")
    public ResponseEntity<?> getByLanguage(@RequestParam String id,
                                           @RequestHeader("Authorization") String authHeader){
        try {
            LangageDto language = interviewService.getLanguage(authHeader,id);
            return ResponseEntity.ok().body(Map.of("language" , language));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Unexpected error" + e.getMessage()));
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdmin(@RequestParam String id){
        try {
            String email = interviewService.getAdmin(id);
            return ResponseEntity.ok().body(Map.of("email" , email));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Unexpected error" + e.getMessage()));
        }
    }

}
