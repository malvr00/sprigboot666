package com.szs.yongil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.dto.test.MemberTestLoginDto;
import com.szs.yongil.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberAuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void loginTest() throws Exception {
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

        // then
        mvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("비밀번호 실패 테스트")
    public void login401FailTest() throws Exception {
        // given
        MemberSignDto signDto = new MemberSignDto();
        signDto.setUserId("test");
        signDto.setPassword("test");
        signDto.setName("홍길동");
        signDto.setRegNo("860824-1655068");

        MemberTestLoginDto loginDto = new MemberTestLoginDto();
        loginDto.setUserId("test");
        loginDto.setPassword("test1");

        // when
        memberService.signup(signDto);

        // then
        mvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("로그인 DTO 미입력 테스트")
    public void login400FailTest() throws Exception {
        // given
        MemberSignDto signDto = new MemberSignDto();
        signDto.setUserId("test");
        signDto.setPassword("test");
        signDto.setName("홍길동");
        signDto.setRegNo("860824-1655068");

        MemberTestLoginDto loginDto = new MemberTestLoginDto();
        loginDto.setUserId("test");

        // when
        memberService.signup(signDto);

        // then
        mvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}
