package com.code_room.interview_service.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  public void sendMessage(String topic, Object message) {
    kafkaTemplate.send(topic, message);
  }
}
