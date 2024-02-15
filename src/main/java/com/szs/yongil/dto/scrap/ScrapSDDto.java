package com.szs.yongil.dto.scrap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ScrapSDDto {

    private BigDecimal premium;
    private BigDecimal medical;
    private BigDecimal salaryT;
    private BigDecimal education;
    private BigDecimal donation;

}
