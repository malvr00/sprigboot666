package com.szs.yongil.entity;

import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class MemberEntityTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AES128Config aes128Config;

    /**
     * 멤버 저장 테스트
     */
    @Test
    public void saveEntity() {
        // given
        MemberEntity memberA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .regNo(aes128Config.encryptAes("910411-1656116"))
                .build();

        // when
        MemberEntity saveMember = memberRepository.save(memberA);

        // then
        Assertions.assertThat(memberA.getId()).isEqualTo(saveMember.getId());
    }
}
