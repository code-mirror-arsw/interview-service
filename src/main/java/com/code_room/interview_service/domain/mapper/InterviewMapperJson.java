package com.code_room.interview_service.domain.mapper;

import com.code_room.interview_service.infrastructure.controller.dto.InterviewEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.util.*;

public class InterviewMapperJson {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static InterviewEventDto mapJsonToInterviewEvent(String jsonString) {
        try {
            if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                jsonString = objectMapper.readTree(jsonString).asText();
            }

            JsonNode rootNode = objectMapper.readTree(jsonString);

            String offerId = rootNode.path("offerId").asText();
            String adminEmail = rootNode.path("adminEmail").asText();
            String description = rootNode.path("description").asText();

            JsonNode acceptedUsersNode = rootNode.path("acceptedUsers");
            List<String> acceptedUsers = new ArrayList<>();
            if (acceptedUsersNode.isArray()) {
                for (JsonNode node : acceptedUsersNode) {
                    acceptedUsers.add(node.asText());
                }
            }
            return InterviewEventDto.builder()
                    .offerId(offerId)
                    .adminEmail(adminEmail)
                    .acceptedUsers(acceptedUsers)
                    .description(description)
                    .build();

        } catch (JsonProcessingException e) {
            System.err.println("❌ Error parsing InterviewEventDto JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
