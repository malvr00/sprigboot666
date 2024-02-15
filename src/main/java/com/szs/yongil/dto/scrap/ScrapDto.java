package com.szs.yongil.dto.scrap;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ScrapDto {

    private BigDecimal taxCredit;
    private BigDecimal premium;
    private BigDecimal medical;
    private BigDecimal education;
    private BigDecimal donation;
    private BigDecimal pension;

}
