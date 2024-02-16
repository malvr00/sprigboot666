package com.szs.yongil.util.scrap;

import com.szs.yongil.common.Const;
import com.szs.yongil.dto.scrap.ScrapSDDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Slf4j
@Component
public class ScrapUtil {


    /**
     * 근로소득세액공제금액 계산 메소드
     * @param argTaxCredit: 산출세액
     */
    public BigDecimal getTaxCredit(BigDecimal argTaxCredit) {
        return argTaxCredit.multiply(Const.CALCULATE.CREDIT);
    }

    /**
     * 보험료공제금액 계산 메소드
     * @param argPremium: 보험료
     */
    public BigDecimal getInsuranceDeduction(BigDecimal argPremium) {
        return argPremium.multiply(Const.CALCULATE.INSURANCE_DEDUCTION);
    }

    /**
     * 의료비공제금액 계산 메소드
     * @param argMedical: 의료비
     * @param argSalaryT: 총 급여
     */
    public BigDecimal getMedicalDeduction(BigDecimal argMedical, BigDecimal argSalaryT) {
        BigDecimal calMS = argSalaryT.multiply(Const.CALCULATE.MEDICAL_PERCENT_1);
        BigDecimal subtract = argMedical.subtract(calMS);
        BigDecimal totalCal = subtract.multiply(Const.CALCULATE.MEDICAL_PERCENT_2);
        return totalCal.compareTo(BigDecimal.ZERO) > 0 ? totalCal : BigDecimal.ZERO;
    }

    /**
     * 교육비공제금액 계산 메소드
     * @param argEducation: 교육비
     */
    public BigDecimal getEducationDeduction(BigDecimal argEducation) {
        return argEducation.multiply(Const.CALCULATE.MEDICAL_PERCENT_2);
    }

    /**
     * 기부금공제금액 계산 메소드
     * @param argDonation: 기부금
     */
    public BigDecimal getDonationDeduction(BigDecimal argDonation) {
        return argDonation.multiply(Const.CALCULATE.MEDICAL_PERCENT_2);
    }

    /**
     * 특별세액공제금액 합 계산 메소드
     * @param argSDDto: 특별공제 계산에 필요한 class
     */
    public BigDecimal getSpecialDeduction(ScrapSDDto argSDDto) {
        return getInsuranceDeduction(argSDDto.getPremium())
                .add(getMedicalDeduction(argSDDto.getMedical(), argSDDto.getSalaryT()))
                .add(getEducationDeduction(argSDDto.getEducation()))
                .add(getDonationDeduction(argSDDto.getDonation()));
    }

    /**
     * 특별세액공제금액 금액 출력 메소드
     * @param argStandard: 표준세액공제금액
     * @param argSDDto: 특별공제 계산에 필요한 class
     */
    public BigDecimal getSpecialDeduction(BigDecimal argStandard, ScrapSDDto argSDDto) {
        return argStandard.compareTo(Const.CALCULATE.BASE_PRICE) == 0
                ? BigDecimal.ZERO : getSpecialDeduction(argSDDto);
    }

    /**
     * 표준세액공제금액 계산 메소드
     * @param argSDDto: 표준세액공제 계산에 필요한 class ( 특별공제계산을 위한 class )
     */
    public BigDecimal getStandardTaxDeduction(ScrapSDDto argSDDto) {
        return getSpecialDeduction(argSDDto).compareTo(Const.CALCULATE.BASE_PRICE) >= 0
                ? BigDecimal.ZERO : Const.CALCULATE.BASE_PRICE;
    }

    /**
     * 퇴직연금세액공제금액 계산 메소드
     * @param argPension: 퇴직연금
     */
    public BigDecimal getRetirementDeduction(BigDecimal argPension) {
        return argPension.multiply(Const.CALCULATE.MEDICAL_PERCENT_2);
    }

    /**
     * 결정세액 2차 계산 메소드
     * @param argAmount: 1차 결정세액 계산 금액
     */
    public BigDecimal getDeterminedTaxAmount(BigDecimal argAmount) {
        return argAmount.compareTo(BigDecimal.ZERO) > 0 ? argAmount : BigDecimal.ZERO;
    }

    /**
     * 금액 포맷 변경 메소드
     * @param argPrice: 변환 할 변수
     */
    public String getCastPrice(BigDecimal argPrice) {
        DecimalFormat format = new DecimalFormat(Const.PATTERN.TEN_MILLION_PATTERN);
        return format.format(argPrice);
    }

}
