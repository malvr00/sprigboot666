package com.szs.yongil.vo.tax;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxVO {

    @Schema(description = "조회 대상 이름", nullable = true, example = "홍길동")
    private String 이름;

    @Schema(description = "결정세액 금액", nullable = true, example = "150,000")
    private String 결정세액;

    @Schema(description = "퇴직연금세액공제", nullable = true, example = "75,000")
    private String 퇴직연금세액공제;

}
