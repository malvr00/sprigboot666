package com.szs.yongil.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberLoginDto {

    @NotNull(message = "User ID does not exit")
    private String userId;

    @NotNull(message = "User password does not exit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
