package com.szs.yongil.service.member;

import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.dto.member.MemberDto;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.repository.member.MemberRepository;
import com.szs.yongil.vo.member.MemberSignVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepo;
    private final AES128Config aes128Config;

    /**
     * 유저 회원가입
     * @param argMemberSignDto: 회원가입 폼
     * @return MemberSignVO.class: memberId, userId
     */
    @Transactional
    public MemberSignVO signup(MemberSignDto argMemberSignDto) {
        MemberSignVO result = null;
        try{
            MemberEntity memberEntity = MemberEntity
                    .builder()
                    .userId(argMemberSignDto.getUserId())
                    .name(argMemberSignDto.getName())
                    .password(passwordEncoder.encode(argMemberSignDto.getPassword()))
                    .regNo(aes128Config.encryptAes(argMemberSignDto.getRegNo()))
                    .build();

            memberRepo.save(memberEntity);

            result = new MemberSignVO(memberEntity.getId(), memberEntity.getUserId());

        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * userId로 유저 조회
     * @param argUserId: 조회 할 userId
     * @return MemberDto.class
     */
    @Override
    public MemberDto getUserDetailsByUserId(String argUserId) {
        Optional<MemberEntity> byMemberEntity = memberRepo.findByUserId(argUserId);

        if (byMemberEntity.isEmpty()) {
            throw new UsernameNotFoundException(argUserId);
        }

        MemberEntity memberEntity = byMemberEntity.get();

        return MemberDto
                .builder()
                .id(memberEntity.getId())
                .userId(memberEntity.getUserId())
                .name(memberEntity.getName())
                .password(memberEntity.getPassword())
                .regNo(memberEntity.getRegNo())
                .build();
    }

    /**
     * 멤버 엔티티 조회 기능
     * @param argMemberId: 조회 할 member_id
     * @return
     */
    @Override
    public MemberEntity getMemberEntityByUserId(Long argMemberId) {
        MemberEntity result = null;
        try {
            Optional<MemberEntity> byId = memberRepo.findById(argMemberId);
            if (byId.isPresent()) {
                result = byId.get();
            }
        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 유저 로그인
     * @param username the username identifying the user whose data is required.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> byMemberEntity = memberRepo.findByUserId(username);

        if (byMemberEntity.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        MemberEntity memberEntity = byMemberEntity.get();

        return new User(memberEntity.getUserId(), memberEntity.getPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}
