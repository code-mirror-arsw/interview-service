package com.code_room.interview_service.domain.usecase;

import com.code_room.interview_service.domain.ports.EncryptService;
import com.code_room.interview_service.infrastructure.Config.EncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptServiceImpl implements EncryptService {

    @Autowired
    private EncryptionProperties encryptionProperties;

    @Override
    public String encrypt(String plainText) throws Exception {
        byte[] keyBytes = encryptionProperties.getStaticKey().getBytes("UTF-8");

        // Generar IV aleatorio
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[16];
        sr.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Preparar Cipher
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, encryptionProperties.getAlgorithm());
        Cipher cipher = Cipher.getInstance(encryptionProperties.getTransformation());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // Encriptar
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

        // Combinar IV + encrypted
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        // Codificar en Base64 URL-safe
        return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
    }

}
