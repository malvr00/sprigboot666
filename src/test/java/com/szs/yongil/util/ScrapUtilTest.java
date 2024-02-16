package com.szs.yongil.util;

import com.szs.yongil.common.Const;
import com.szs.yongil.dto.scrap.ScrapSDDto;
import com.szs.yongil.util.scrap.ScrapUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class ScrapUtilTest {

    @Autowired
    ScrapUtil scrapUtil;

    @Test
    @DisplayName("근로소득세액공제금액_테스트")
    public void getTaxCreditTest() {
        // given
        BigDecimal val = new BigDecimal("100");

        // when
        BigDecimal taxCredit = scrapUtil.getTaxCredit(val);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("55"));

    }

    @Test
    @DisplayName("보험료공제금액_테스트")
    public void getInsuranceDeductionTest() {
        // given
        BigDecimal val = new BigDecimal("100");

        // when
        BigDecimal taxCredit = scrapUtil.getInsuranceDeduction(val);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("12"));

    }

    @Test
    @DisplayName("의료비공제금액_테스트")
    public void getMedicalDeduction1Test() {
        // given
        BigDecimal val1 = new BigDecimal("100");
        BigDecimal val2 = new BigDecimal("1000");

        // when
        BigDecimal taxCredit = scrapUtil.getMedicalDeduction(val1, val2);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("10.5"));

    }

    @Test
    @DisplayName("의료비공제금액_0원처리_테스트")
    public void getMedicalDeduction2Test() {
        // given
        BigDecimal val1 = new BigDecimal("20");
        BigDecimal val2 = new BigDecimal("1000");

        // when
        BigDecimal taxCredit = scrapUtil.getMedicalDeduction(val1, val2);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("0"));

    }

    @Test
    @DisplayName("교육비공제금액_테스트")
    public void getEducationDeductionTest() {
        // given
        BigDecimal val1 = new BigDecimal("100");

        // when
        BigDecimal taxCredit = scrapUtil.getEducationDeduction(val1);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("15"));

    }

    @Test
    @DisplayName("기부금공제금액_테스트")
    public void getDonationDeductionTest() {
        // given
        BigDecimal val1 = new BigDecimal("100");

        // when
        BigDecimal taxCredit = scrapUtil.getDonationDeduction(val1);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("15"));

    }

    @Test
    @DisplayName("특별세액공제금액_합_테스트")
    public void getSpecialDeduction1Test() {
        // given
        ScrapSDDto scrapSDDto = new ScrapSDDto(new BigDecimal("100"), new BigDecimal("100"),
                new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("100"));

        // when
        BigDecimal taxCredit = scrapUtil.getSpecialDeduction(scrapSDDto);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("56.55"));

    }

    @Test
    @DisplayName("특별세액공제금액_합_테스트")
    public void getSpecialDeduction2Test() {
        // given
        ScrapSDDto scrapSDDto = new ScrapSDDto(new BigDecimal("100"), new BigDecimal("100"),
                new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("100"));

        // when
        BigDecimal taxCredit = scrapUtil.getSpecialDeduction(Const.CALCULATE.BASE_PRICE, scrapSDDto);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("0"));

    }

    @Test
    @DisplayName("특별세액공제금액_금액_테스트")
    public void getSpecialDeduction3Test() {
        // given
        ScrapSDDto scrapSDDto = new ScrapSDDto(new BigDecimal("100"), new BigDecimal("100"),
                new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("100"));

        // when
        BigDecimal taxCredit = scrapUtil.getSpecialDeduction(BigDecimal.ZERO, scrapSDDto);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("56.55"));

    }

    @Test
    @DisplayName("표준세액공제금액_금액_13만원_테스트")
    public void getStandardTaxDeduction1Test() {
        // given
        ScrapSDDto scrapSDDto = new ScrapSDDto(new BigDecimal("100"), new BigDecimal("100"),
                new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("100"));

        // when
        BigDecimal taxCredit = scrapUtil.getStandardTaxDeduction(scrapSDDto);

        // then
        Assertions.assertThat(taxCredit).isEqualTo(Const.CALCULATE.BASE_PRICE);

    }

    @Test
    @DisplayName("표준세액공제금액_금액_0원_테스트")
    public void getStandardTaxDeduction2Test() {
        // given
        ScrapSDDto scrapSDDto = new ScrapSDDto(new BigDecimal("123124124"), new BigDecimal("223213"),
                new BigDecimal("10000000"), new BigDecimal("1231223"), new BigDecimal("12323"));

        // when
        BigDecimal taxCredit = scrapUtil.getStandardTaxDeduction(scrapSDDto);

        // then
        Assertions.assertThat(taxCredit).isEqualTo(BigDecimal.ZERO);

    }

    @Test
    @DisplayName("퇴직연금세액공제금액_테스트")
    public void getRetirementDeductionTest() {
        // given
        BigDecimal val = new BigDecimal("100");

        // when
        BigDecimal taxCredit = scrapUtil.getRetirementDeduction(val);

        // then
        Assertions.assertThat(taxCredit.stripTrailingZeros()).isEqualTo(new BigDecimal("15"));

    }

    @Test
    @DisplayName("결정세액_0초과_테스트")
    public void getDeterminedTaxAmount1Test() {
        // given
        BigDecimal val = new BigDecimal("100");

        // when
        BigDecimal taxCredit = scrapUtil.getDeterminedTaxAmount(val);

        // then
        Assertions.assertThat(taxCredit).isEqualTo(new BigDecimal("100"));

    }

    @Test
    @DisplayName("결정세액_음수_테스트")
    public void getDeterminedTaxAmount2Test() {
        // given
        BigDecimal val = new BigDecimal("-100");

        // when
        BigDecimal taxCredit = scrapUtil.getDeterminedTaxAmount(val);

        // then
        Assertions.assertThat(taxCredit).isEqualTo(BigDecimal.ZERO);

    }

    @Test
    @DisplayName("금액_포맷변경_테스트")
    public void getCastPriceTest() {
        // given
        BigDecimal val = new BigDecimal("100000");

        // when
        String castPrice = scrapUtil.getCastPrice(val);

        // then
        Assertions.assertThat(castPrice).isEqualTo("100,000");

    }
}
