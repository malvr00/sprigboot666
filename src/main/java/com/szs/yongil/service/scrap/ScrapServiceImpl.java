package com.szs.yongil.service.scrap;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.domain.scrap.ScrapEntity;
import com.szs.yongil.repository.member.MemberRepository;
import com.szs.yongil.repository.scrap.ScrapRepository;
import com.szs.yongil.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final MemberService memberService;
    private final ScrapRepository scrapRepo;

    @Override
    @Transactional
    public Long saveScrapData(ScrapEntity argScrapEntity, Long argUserId) {
        ScrapEntity result = null;
        try {
            MemberEntity memberEntity = memberService.getMemberEntityByUserId(argUserId);
            argScrapEntity.setMemberData(memberEntity);
            result = scrapRepo.save(argScrapEntity);
        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result.getId();
    }
}
