package com.szs.yongil.Service;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.service.member.MemberService;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.member.MemberSignVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void signSuccessTest() throws ApiCustomException {
        // given
        MemberSignDto memberSignDto = new MemberSignDto();
        memberSignDto.setUserId("hong12");
        memberSignDto.setName("홍길동");
        memberSignDto.setPassword("test");
        memberSignDto.setRegNo("910411-1656116");

        // when
        MemberSignVO signup = memberService.signup(memberSignDto);

        // then
        Assertions.assertThat(memberSignDto.getUserId()).isEqualTo(signup.getUserId());
    }

    @Test
    public void signFailTest() throws ApiCustomException {
        // given
        MemberSignDto memberSignA = new MemberSignDto();
        memberSignA.setUserId("hong12");
        memberSignA.setName("홍길동");
        memberSignA.setPassword("test");
        memberSignA.setRegNo("910411-1656116");

        MemberSignDto memberSignB = new MemberSignDto();
        memberSignA.setUserId("hong12");
        memberSignA.setName("홍길동");
        memberSignA.setPassword("test");
        memberSignA.setRegNo("910411-1656116");

        // when
        MemberSignVO signup = memberService.signup(memberSignA);

        // then
        Assertions.assertThat(memberSignA.getUserId()).isEqualTo(signup.getUserId());
    }

}
