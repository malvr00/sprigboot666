package com.szs.yongil.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginVO {

    @Schema(description = "JWT Token", nullable = true, example = "jwt.token.response")
    private String accessToken;

}
