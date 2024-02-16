package com.szs.yongil.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberSignDto {

    @NotNull(message = "User ID does not exit")
    @Schema(description = "사용자 아이디", nullable = true, example = "test")
    private String userId;

    @NotNull(message = "User password does not exit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "사용자 패스워드", nullable = true, example = "test")
    private String password;

    @NotNull(message = "User name does not exit")
    @Schema(description = "사용자 이름", nullable = true, example = "홍길동")
    private String name;

    @Size(min = 14, max = 14)
    @NotNull(message = "User regNo does not exit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "사용자 주민등록 번호", nullable = true, example = "860824-1655068")
    private String regNo;
}
