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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final MemberService memberService;
    private final ScrapRepository scrapRepo;

    /**
     * 스크래핑 정보 저장 메소드
     * @param argScrapEntity: 저장 할 데이터
     * @param memberId: 연관관계 멤버 아이디
     */
    @Override
    @Transactional
    public ScrapEntity saveScrapData(ScrapEntity argScrapEntity, Long memberId) {
        ScrapEntity result = null;
        try {
            MemberEntity memberEntity = memberService.getMemberEntityByMemberId(memberId);
            ScrapEntity findScrapEntity = findByMember(memberEntity);
            if (findScrapEntity == null) {
                argScrapEntity.setMemberData(memberEntity);
                result = scrapRepo.save(argScrapEntity);
            } else {
                findScrapEntity.setMemberData(memberEntity);
                findScrapEntity.updateScrap(argScrapEntity);
                result = findScrapEntity;
            }
        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 멤버엔티티로 스크래핑 엔티티 조회 메소드
     * @param memberEntity: 연관관계 멤버 엔티티
     */
    @Override
    public ScrapEntity findByMember(MemberEntity memberEntity) {
        ScrapEntity result = null;
        try {
            Optional<ScrapEntity> byMember = scrapRepo.findByMember(memberEntity);
            if (byMember.isPresent()) {
                result = byMember.get();
            }
        } catch (JpaSystemException e) {
            log.error("{}", e.getLocalizedMessage());
        }
        return result;
    }
}
