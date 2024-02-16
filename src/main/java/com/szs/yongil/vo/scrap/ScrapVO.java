package com.szs.yongil.vo.scrap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScrapVO {

    @Schema(description = "사용자 테이블 고유 아이디", nullable = true, example = "1")
    private Long memberId;

    @Schema(description = "스크랩 아이디", nullable = true, example = "1")
    private Long scrapId;

}
