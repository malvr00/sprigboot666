package com.szs.yongil.dto.test;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberTestLoginDto {

    /**
     *  MemberLoginDto로 테스트 코드 진행 시 @JsonProperty 어노테이션 이슈로 Body에서 password가 제외되는
     *  문제가 있어서 별보도로 작성
     */
    @NotNull(message = "User ID does not exit")
    private String userId;

    @NotNull(message = "User password does not exit")
    private String password;
}
