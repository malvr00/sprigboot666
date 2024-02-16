package com.szs.yongil.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberLoginDto {

    @NotNull(message = "User ID does not exit")
    @Schema(description = "사용자 아이디", nullable = true, example = "test")
    private String userId;

    @NotNull(message = "User password does not exit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "사용자 패스워드", nullable = true, example = "test")
    private String password;

}
