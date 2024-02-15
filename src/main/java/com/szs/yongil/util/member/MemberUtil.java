package com.szs.yongil.util.member;

import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;
    private final MemberRegisterUtil memberRegisterUtil;
    private final AES128Config aes128Config;

    /**
     * 유저 중복 회원가입 체크
     * @param argName: 조회 할 이름
     * @param argRegNo: 조회 할 주민등록 번호
     * @return boolean
     */
    public boolean userCheck(String argName, String argRegNo) {
        boolean result = false;
        try {
            List<MemberEntity> members = memberRepo.findByName(argName);

            if (!members.isEmpty()) {
                for (MemberEntity tmp : members) {
                    if (aes128Config.decryptAes(tmp.getRegNo()).equals(argRegNo)) {
                        return true;
                    }
                }
            }
        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 중복 닉네임 확인
     * @param argUserId: 조회 할 아이디
     * @return boolean
     */
    public boolean userIdDuplicateCheck(String argUserId) {
        boolean result = false;

        try {
            Optional<MemberEntity> memberEntity = memberRepo.findByUserId(argUserId);

            if (memberEntity.isPresent()) {
                return true;
            }
        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 특정 사용자 확인
     * @param argName: 가입을 진행 할 이름
     * @param argRegNo: 가입을 진행 할 주민등록번호
     * @return boolean
     */
    public boolean userValidationCheck(String argName, String argRegNo) {
        boolean result = false;
        if (memberRegisterUtil.getUserTableMap().size() == 0) {
            return false;
        }
        String userInfo = memberRegisterUtil.getUserTableMap().get(argName);

        if (userInfo != null && userInfo.equals(argRegNo)) {
            result = true;
        }

        return result;
    }
}
