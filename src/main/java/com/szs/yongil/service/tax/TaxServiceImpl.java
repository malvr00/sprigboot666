package com.szs.yongil.service.tax;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.domain.scrap.ScrapEntity;
import com.szs.yongil.dto.scrap.ScrapSDDto;
import com.szs.yongil.service.member.MemberService;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.util.exception.enums.ErrorEnum;
import com.szs.yongil.util.scrap.ScrapUtil;
import com.szs.yongil.vo.tax.TaxVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final MemberService memberService;
    private final ScrapUtil scrapUtil;

    @Override
    public TaxVO getRefundData() throws ApiCustomException {
        TaxVO result = null;

        // 유저 검증
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberEntity memberEntity = memberService.getMemberEntityByUserId(userDetails.getUsername());

        // Scrap 검증
        ScrapEntity scrapEntity = memberEntity.getScrapEntity();
        if (scrapEntity == null) {
            throw new ApiCustomException(ErrorEnum.NOT_SCRAP_DATA);
        }

        try { // Refund 계산
            ScrapSDDto scrapSDDto = new ScrapSDDto(scrapEntity.getPremium(),
                    scrapEntity.getMedical(), scrapEntity.getSalaryT(),
                    scrapEntity.getEducation(), scrapEntity.getDonation());

            // 산출세액
            BigDecimal taxCredit = scrapEntity.getTaxCredit();
            // 근로소득세액공제금액
            BigDecimal calTaxCredit = scrapUtil.getTaxCredit(taxCredit);
            // 표준세액공제금액
            BigDecimal standardTaxDeduction = scrapUtil.getStandardTaxDeduction(scrapSDDto);
            // 특별세액공제금액
            BigDecimal baseSpecialDeduction = scrapUtil.getSpecialDeduction(standardTaxDeduction, scrapSDDto);
            // 퇴직연금세액공제금액
            BigDecimal retirementDeduction = scrapUtil.getRetirementDeduction(scrapEntity.getPension());
            // 결정세액 1차 계산
            BigDecimal baseAmount = taxCredit.subtract(calTaxCredit).subtract(baseSpecialDeduction)
                    .subtract(standardTaxDeduction).subtract(retirementDeduction);
            // 결정세액 2차 계산
            BigDecimal answerAmount = scrapUtil.getDeterminedTaxAmount(baseAmount);

            result = new TaxVO(memberEntity.getName(), scrapUtil.getCastPrice(answerAmount.stripTrailingZeros()),
                    scrapUtil.getCastPrice(retirementDeduction.stripTrailingZeros()));

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException("getRefund Error");
        }

        return result;
    }
}
