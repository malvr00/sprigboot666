package com.szs.yongil.service.member;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.repository.member.MemberRepository;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.member.MemberSignVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepo;

    /**
     * 유저 회원가입
     * @param memberSignDto: 회원가입 폼
     * @return MemberSignVO.class: memberId, userId
     */
    @Transactional
    public MemberSignVO signup(MemberSignDto memberSignDto) {
        MemberSignVO result = null;
        try{
            MemberEntity memberEntity = MemberEntity
                    .builder()
                    .userId(memberSignDto.getUserId())
                    .name(memberSignDto.getName())
                    .password(passwordEncoder.encode(memberSignDto.getPassword()))
                    .regNo(passwordEncoder.encode(memberSignDto.getRegNo()))
                    .build();

            memberRepo.save(memberEntity);

            result = new MemberSignVO(memberEntity.getId(), memberEntity.getUserId());

        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }

}
