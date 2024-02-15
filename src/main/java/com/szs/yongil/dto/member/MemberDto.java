package com.szs.yongil.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {

    private Long id;
    private String userId;
    private String name;
    private String password;
    private String regNo;
}
