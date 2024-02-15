package com.szs.yongil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.yongil.common.jwt.TokenProvider;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.dto.test.MemberTestLoginDto;
import com.szs.yongil.dto.test.MemberTestSignDto;
import com.szs.yongil.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberPostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 테스트")
    public void signTest() throws Exception {
        // given
        MemberTestSignDto signDto = new MemberTestSignDto();
        signDto.setUserId("test");
        signDto.setPassword("test");
        signDto.setName("홍길동");
        signDto.setRegNo("860824-1655068");

        // when

        // then
        mvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signDto)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원가입 DTO 미입력 실패 테스트")
    public void signFail1Test() throws Exception {
        // given
        MemberTestSignDto signDtoA = new MemberTestSignDto();
        signDtoA.setUserId("test");
        signDtoA.setPassword("test");
        signDtoA.setName("홍길동");

        // when

        // then
        mvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signDtoA)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("중복 회원가입 실패 테스트")
    public void signFail2Test() throws Exception {
        // given
        MemberTestSignDto signDtoA = new MemberTestSignDto();
        signDtoA.setUserId("test");
        signDtoA.setPassword("test");
        signDtoA.setName("홍길동");
        signDtoA.setRegNo("860824-1655068");

        MemberSignDto signDtoB = new MemberSignDto();
        signDtoB.setUserId("test");
        signDtoB.setPassword("test");
        signDtoB.setName("홍길동");
        signDtoB.setRegNo("860824-1655068");

        // when
        memberService.signup(signDtoB);

        // then
        mvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signDtoA)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("특정 사용자 회원가입 실패 테스트")
    public void signFail3Test() throws Exception {
        // given
        MemberTestSignDto signDtoA = new MemberTestSignDto();
        signDtoA.setUserId("test");
        signDtoA.setPassword("test");
        signDtoA.setName("테스트");
        signDtoA.setRegNo("860824-1655068");

        // when

        // then
        mvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signDtoA)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("스크래핑 API 테스트")
    public void scrapApiTest() throws Exception {
        // given
        MemberSignDto signDto = new MemberSignDto();
        signDto.setUserId("test");
        signDto.setPassword("test");
        signDto.setName("홍길동");
        signDto.setRegNo("860824-1655068");

        MemberTestLoginDto loginDto = new MemberTestLoginDto();
        loginDto.setUserId("test");
        loginDto.setPassword("test");

        // when
        memberService.signup(signDto);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        String header = "Bearer " + jwt;

        // then
        mvc.perform(post("/szs/scrap")
                        .header("Authorization", header))
                        .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("스크래핑 API 실패 테스트( 토큰 없을 경우 )")
    public void scrapApiFail1Test() throws Exception {
        // given

        // when

        // then
        mvc.perform(post("/szs/scrap"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

}
