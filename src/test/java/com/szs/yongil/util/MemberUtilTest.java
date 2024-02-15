package com.szs.yongil.util;

import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.repository.member.MemberRepository;
import com.szs.yongil.util.member.MemberUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class MemberUtilTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AES128Config aes128Config;

    @Autowired
    MemberUtil memberUtil;

    /**
     * 중복 유저 아이디 테스트
     */
    @Test
    public void user_duplicate_check_test() {
        // given
        MemberEntity memberSignA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .regNo(aes128Config.encryptAes("910411-1656116"))
                .build();

        MemberSignDto memberSignB = new MemberSignDto();
        memberSignB.setUserId("hong12");
        memberSignB.setName("홍길동");
        memberSignB.setPassword("test");
        memberSignB.setRegNo("910411-1656116");

        // when
        MemberEntity save = memberRepository.save(memberSignA);
        boolean duplicate = memberUtil.userIdDuplicateCheck(memberSignB.getUserId());

        // then
        Assertions.assertThat(duplicate).isEqualTo(true);
    }

    /**
     * 중복 회원가입 테스트
     */
    @Test
    public void user_sign_check_test() {
        // given
        MemberEntity memberSignA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .regNo(aes128Config.encryptAes("910411-1656116"))
                .build();

        MemberSignDto memberSignB = new MemberSignDto();
        memberSignB.setUserId("hong12");
        memberSignB.setName("홍길동");
        memberSignB.setPassword("test");
        memberSignB.setRegNo("910411-1656116");

        // when
        MemberEntity save = memberRepository.save(memberSignA);
        boolean duplicate = memberUtil.userCheck(memberSignB.getName(), memberSignB.getRegNo());

        // then
        Assertions.assertThat(duplicate).isEqualTo(true);
    }

    /**
     * 특정 사용자 검증 테스트
     */
    @Test
    public void user_validation_check_test() {
        // given
        MemberEntity memberSignA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password("test")
                .regNo("910411-1656116")
                .build();

        MemberEntity memberSignB = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password("test")
                .regNo("860824-1655068")
                .build();

        // when
        boolean checkA = memberUtil.userValidationCheck(memberSignA.getName(), memberSignA.getRegNo());
        boolean checkB = memberUtil.userValidationCheck(memberSignB.getName(), memberSignB.getRegNo());

        // then
        // 존재하지 않는 사용자 정보
        Assertions.assertThat(checkA).isEqualTo(false);

        // 존재하는 사용자 정보
        Assertions.assertThat(checkB).isEqualTo(true);
    }
}
