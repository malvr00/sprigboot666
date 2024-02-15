package com.szs.yongil.controller.member;

import com.szs.yongil.service.tax.TaxService;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.tax.TaxVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @GetMapping(value = "/refund", produces = "application/json; charset=utf-8")
    public ResponseEntity<TaxVO> refund() throws ApiCustomException {
        TaxVO result = taxService.getRefundData();
        return ResponseEntity.ok(result);
    }
}
