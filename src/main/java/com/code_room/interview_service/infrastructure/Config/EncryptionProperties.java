package com.code_room.interview_service.infrastructure.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {
    private String algorithm;
    private String transformation;
    private int keySize;
    private String staticKey;
    private String link;
}
