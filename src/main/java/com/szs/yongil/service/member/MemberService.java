package com.szs.yongil.service.member;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.dto.member.MemberDto;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.vo.member.MemberSignVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

    MemberSignVO signup(MemberSignDto argMemberSignDto);

    MemberDto getUserDetailsByUserId(String argUserId);

    MemberEntity getMemberEntityByMemberId(Long argMemberId);

    MemberEntity getMemberEntityByUserId(String argUserId);
}
