package com.code_room.interview_service.infrastructure.listener;


import com.code_room.interview_service.domain.mapper.InterviewMapperJson;
import com.code_room.interview_service.domain.ports.InterviewService;
import com.code_room.interview_service.infrastructure.controller.dto.InterviewEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationListener {

  @Autowired
  InterviewService interviewService;

  @KafkaListener(
          topics = "${kafka.topic.name}",
          groupId = "${kafka.group.id}"
  )
  public void listen(String message) throws Exception {
    System.out.println("📨 Mensaje recibido de Kafka: " + message);

    try {
      InterviewEventDto json = InterviewMapperJson.mapJsonToInterviewEvent(message);
      interviewService.handleAcceptedApplicants(json);
    } catch (Exception e) {
      System.err.println("❌ Error al convertir mensaje a InterviewEventDto: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
