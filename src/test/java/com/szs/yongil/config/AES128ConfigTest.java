package com.szs.yongil.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AES128ConfigTest {

    @Autowired
    private AES128Config aes128Config;

    @Test
    @DisplayName("Aes128 암호화 테스트")
    void aes128Test() {
        String text = "test text";
        String enc = aes128Config.encryptAes(text);
        String dec = aes128Config.decryptAes(enc);

        Assertions.assertThat(dec).isEqualTo(text);
    }
}
