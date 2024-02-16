package com.szs.yongil.controller;

import com.szs.yongil.common.jwt.TokenProvider;
import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.dto.test.MemberTestLoginDto;
import com.szs.yongil.repository.member.MemberRepository;
import com.szs.yongil.service.client.ClientService;
import com.szs.yongil.vo.scrap.ScrapVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TaxControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AES128Config aes128Config;

    @Test
    @DisplayName("사용자 결정세액 조회 API 테스트")
    public void refundApiTest() throws Exception {
        // given
        MemberEntity memberA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .regNo(aes128Config.encryptAes("860824-1655068"))
                .build();

        MemberTestLoginDto loginDto = new MemberTestLoginDto();
        loginDto.setUserId("hong12");
        loginDto.setPassword("test");

        // when
        memberRepository.save(memberA);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        String header = "Bearer " + jwt;

        clientService.getScrapData();

        // then
        mvc.perform(get("/szs/refund")
                        .header("Authorization", header))
                .andDo(print())
                .andExpect(status().isOk());
    } 
}
