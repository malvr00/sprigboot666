package com.szs.yongil.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static com.szs.yongil.common.Const.AE128.ENCODING_TYPE;
import static com.szs.yongil.common.Const.AE128.INSTANCE_TYPE;

@Slf4j
@Component
public class AES128Config {

    /**
     *  주민번호를 위한 양방향 암호화
     */

    @Value("${aes.secret}")
    private String secretKey;
    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    @PostConstruct
    public void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        // SecretKeySpec과 IvParameterSpec에 동일한 키를 사용하게 되면 보안에 취약하기 때문에 SecureRandom을 추가
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];   // 16bytes = 128bits
        secureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
        // 알고리즘 타입 지정
        secretKeySpec = new SecretKeySpec(secretKey.getBytes(ENCODING_TYPE), "AES");
        cipher = Cipher.getInstance(INSTANCE_TYPE);
    }

    /**
     * 암호화
     */
    public String encryptAes(String plaintext) {
        try {
            // secretKeySpec, ivParameterSpec를 사용하여 암호화 모드로 Cipher 초기화
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            // 암호화 후 편의를 위해 Base64로 변환
            byte[] encryted = cipher.doFinal(plaintext.getBytes(ENCODING_TYPE));
            return new String(Base64.getEncoder().encode(encryted), ENCODING_TYPE);
        } catch (Exception e) {
            log.error("{}", e.getLocalizedMessage());
            throw new RuntimeException("EncryptAes exception");
        }
    }

    /**
     * 복호화
     */
    public String decryptAes(String plaintext) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            // 데이터를 복화한다.
            byte[] decoded = Base64.getDecoder().decode(plaintext.getBytes(ENCODING_TYPE));
            return new String(cipher.doFinal(decoded), ENCODING_TYPE);
        } catch (Exception e) {
            log.error("{}", e.getLocalizedMessage());
            throw new RuntimeException("decryptAes exception");
        }
    }
}
