package com.szs.yongil.service.scrap;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.domain.scrap.ScrapEntity;

public interface ScrapService {

    ScrapEntity saveScrapData(ScrapEntity argScrapEntity, Long memberId);

    ScrapEntity findByMember(MemberEntity memberEntity);

}
