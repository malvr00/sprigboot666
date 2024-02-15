package com.szs.yongil.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberSignDto {

    @NotNull(message = "User ID does not exit")
    private String userId;

    @NotNull(message = "User password does not exit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "User name does not exit")
    private String name;

    @Size(min = 14, max = 14)
    @NotNull(message = "User regNo does not exit")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String regNo;
}
