package com.szs.yongil.service.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.yongil.client.SzsClient;
import com.szs.yongil.common.Const;
import com.szs.yongil.common.CustomExceptionHandler;
import com.szs.yongil.config.AES128Config;
import com.szs.yongil.domain.scrap.ScrapEntity;
import com.szs.yongil.dto.client.ScrapClientDto;
import com.szs.yongil.dto.member.MemberDto;
import com.szs.yongil.service.member.MemberService;
import com.szs.yongil.service.scrap.ScrapService;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.util.exception.enums.ErrorEnum;
import com.szs.yongil.vo.scrap.ScrapVO;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final SzsClient szsClient;
    private final MemberService memberService;
    private final ScrapService scrapService;
    private final AES128Config aes128Config;

    /**
     * SZS 스크래핑 가져온 후 성공하면 DB에 저장하는 메소드
     */
    @Override
    public ScrapVO getScrapData() throws ApiCustomException {
        ScrapVO result = null;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberDto memberDto = memberService.getUserDetailsByUserId(userDetails.getUsername());
        ScrapClientDto scrapClientDto = ScrapClientDto
                .builder()
                .name(memberDto.getName())
                .regNo(aes128Config.decryptAes(memberDto.getRegNo()))
                .build();

        Response scrap = null;
        try{
            scrap = szsClient.getScrap(scrapClientDto);
        } catch(Exception e) {
            log.error("{}", e.getLocalizedMessage());
            throw new RuntimeException("FeignClient error");
        }
        if (scrap.status() == 200) {
            result = saveScrap(scrap, memberDto.getId());

            if (result == null) {
                throw new ApiCustomException(ErrorEnum.FEIGN_FAIL);
            }

        }

        return result;
    }

    /**
     * Scrap 데이터 DB에 저장하는 메소드
     * @param argScrap: 저장 할 Scrap 데이터
     * @param argMemberId: 연관관계 멤버 아이디
     */
    private ScrapVO saveScrap(Response argScrap, Long argMemberId) {
        ScrapVO result = null;
        try {
            ObjectMapper mapper = new ObjectMapper();

            Map map = mapper.readValue(argScrap.body().asInputStream(), Map.class);

            if(!map.get("status").equals("success")) return null;

            Map data = (Map) map.get(Const.SCRAP.JSON_DATA);
            Map jsonList = (Map) data.get(Const.SCRAP.JSON_LIST);
            List deduction = (List) jsonList.get(Const.SCRAP.INCOME_DEDUCTION);
            List salary = (List) jsonList.get(Const.SCRAP.SALARY);
            // 산출세액
            String taxCredit = (String) jsonList.get(Const.SCRAP.TAX_CREDIT);
            // 보험료
            String premium = "";
            // 의료비
            String medical = "";
            // 교육비
            String education = "";
            // 기부금
            String donation = "";
            // 퇴직연금
            String pension = "";
            // 총 급여
            String salaryT = "";

            if (!deduction.isEmpty()) {
                for (Object o : deduction) {
                    Map deductionMap = mapper.convertValue(o, Map.class);
                    if (deductionMap.get(Const.SCRAP.INCOME_CLASSIFICATION)
                            .equals(Const.SCRAP.PREMIUM)) {
                        premium = (String) deductionMap.get(Const.SCRAP.AMOUNT);
                    } else if (deductionMap.get(Const.SCRAP.INCOME_CLASSIFICATION)
                            .equals(Const.SCRAP.MEDICAL_EXPENSES)) {
                        medical = (String) deductionMap.get(Const.SCRAP.AMOUNT);
                    } else if (deductionMap.get(Const.SCRAP.INCOME_CLASSIFICATION)
                            .equals(Const.SCRAP.EDUCATION_EXPENSES)) {
                        education = (String) deductionMap.get(Const.SCRAP.AMOUNT);
                    } else if (deductionMap.get(Const.SCRAP.INCOME_CLASSIFICATION)
                            .equals(Const.SCRAP.DONATION)) {
                        donation = (String) deductionMap.get(Const.SCRAP.AMOUNT);
                    } else if (deductionMap.get(Const.SCRAP.INCOME_CLASSIFICATION)
                            .equals(Const.SCRAP.RETIREMENT_PENSION)) {
                        pension = (String) deductionMap.get(Const.SCRAP.T_AMOUNT);
                    }
                }
            }

            if (!salary.isEmpty()) {
                boolean flag = false;
                for (Object o : salary) {
                    if(flag) break;
                    Map salaryMap = mapper.convertValue(o, Map.class);
                    for (Object key : salaryMap.keySet()) {
                        if (key instanceof String) {
                            if (String.valueOf(key).equals(Const.SCRAP.TOTAL_SALARY)) {
                                salaryT = (String) salaryMap.get(key);
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }

            ScrapEntity scrapEntity = ScrapEntity
                    .builder()
                    .taxCredit(new BigDecimal(taxCredit.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .premium(new BigDecimal(premium.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .medical(new BigDecimal(medical.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .education(new BigDecimal(education.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .donation(new BigDecimal(donation.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .pension(new BigDecimal(pension.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .salaryT(new BigDecimal(salaryT.replaceAll(Const.SCRAP.CASTING_COMMA, "")))
                    .build();

            Long scrapId = scrapService.saveScrapData(scrapEntity, argMemberId);

            result = new ScrapVO(argMemberId, scrapId);
        } catch (IOException e) {
            log.error("{}", e.getLocalizedMessage());
        }

        return result;
    }
}
