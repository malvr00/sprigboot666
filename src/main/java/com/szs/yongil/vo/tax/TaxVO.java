package com.szs.yongil.vo.tax;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxVO {

    private String 이름;

    private String 결정세액;

    private String 퇴직연금세액공제;

}
