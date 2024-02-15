package com.szs.yongil.service.scrap;

import com.szs.yongil.domain.scrap.ScrapEntity;

public interface ScrapService {

    Long saveScrapData(ScrapEntity argScrapEntity, Long memberId);
}
