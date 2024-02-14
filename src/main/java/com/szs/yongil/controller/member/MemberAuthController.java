package com.szs.yongil.controller.member;

import com.szs.yongil.common.Const;
import com.szs.yongil.common.jwt.TokenProvider;
import com.szs.yongil.dto.member.MemberLoginDto;
import com.szs.yongil.vo.member.MemberLoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
public class MemberAuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginVO> authorize(
            @Valid @RequestBody MemberLoginDto memberLoginDto
    ) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberLoginDto.getUserId(), memberLoginDto.getPassword());
        // authenticate이 실행이 될 때 MemberService에 loadUserByUsername가 실행이 됨.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Const.JWT_PAYLOAD.HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new MemberLoginVO(jwt), httpHeaders, HttpStatus.OK);
    }
}
