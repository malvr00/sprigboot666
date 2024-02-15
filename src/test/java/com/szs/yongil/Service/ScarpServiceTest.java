package com.szs.yongil.Service;

import com.szs.yongil.common.Const;
import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.domain.scrap.ScrapEntity;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.repository.member.MemberRepository;
import com.szs.yongil.repository.scrap.ScrapRepository;
import com.szs.yongil.service.member.MemberService;
import com.szs.yongil.service.scrap.ScrapService;
import com.szs.yongil.vo.member.MemberSignVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class ScarpServiceTest {

    @Autowired
    ScrapService scrapService;

    @Autowired
    ScrapRepository scrapRepo;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AES128Config aes128Config;

    @Test
    public void scrapSaveTest() {
        // given
        MemberEntity memberA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .regNo(aes128Config.encryptAes("910411-1656116"))
                .build();

        ScrapEntity scrapEntity = ScrapEntity
                .builder()
                .taxCredit(new BigDecimal(100))
                .premium(new BigDecimal(100))
                .medical(new BigDecimal(100))
                .education(new BigDecimal(100))
                .donation(new BigDecimal(100))
                .pension(new BigDecimal(100))
                .salaryT(new BigDecimal(100))
                .build();


        // when
        MemberEntity saveMember = memberRepository.save(memberA);
        Long scrapId = scrapService.saveScrapData(scrapEntity, saveMember.getId());
        ScrapEntity findScrap = scrapRepo.findById(scrapId).get();

        // then
        Assertions.assertThat(scrapId).isEqualTo(findScrap.getId());
    }

    @Test
    @DisplayName("스크래핑 중복 Exception 테스트")
    public void scrapSaveFailTest() {
        // given
        MemberEntity memberA = MemberEntity
                .builder()
                .userId("hong12")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .regNo(aes128Config.encryptAes("910411-1656116"))
                .build();

        ScrapEntity scrapEntityA = ScrapEntity
                .builder()
                .taxCredit(new BigDecimal(100))
                .premium(new BigDecimal(100))
                .medical(new BigDecimal(100))
                .education(new BigDecimal(100))
                .donation(new BigDecimal(100))
                .pension(new BigDecimal(100))
                .salaryT(new BigDecimal(100))
                .build();

        ScrapEntity scrapEntityB = ScrapEntity
                .builder()
                .taxCredit(new BigDecimal(100))
                .premium(new BigDecimal(100))
                .medical(new BigDecimal(100))
                .education(new BigDecimal(100))
                .donation(new BigDecimal(100))
                .pension(new BigDecimal(100))
                .salaryT(new BigDecimal(100))
                .build();


        // when
        MemberEntity saveMember = memberRepository.save(memberA);
        scrapService.saveScrapData(scrapEntityA, saveMember.getId());

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            scrapService.saveScrapData(scrapEntityB, saveMember.getId());
        });
    }
}
