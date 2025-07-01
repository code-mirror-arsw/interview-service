package com.code_room.interview_service.domain.ports;

public interface EncryptService {
    String encrypt(String plainText) throws Exception;
}
