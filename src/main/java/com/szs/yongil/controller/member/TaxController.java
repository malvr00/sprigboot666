package com.szs.yongil.controller.member;

import com.szs.yongil.service.tax.TaxService;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.tax.TaxVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
@Tag(name = "세액 컨트롤러", description = "세액조회 등 세액관련 API 입니다.")
public class TaxController {

    private final TaxService taxService;

    @GetMapping(value = "/refund", produces = "application/json; charset=utf-8")
    @Operation(summary = "사용자 결정세액 조회", description = "사용자 결정세액 조회 API 입니다.")
    public ResponseEntity<TaxVO> refund() throws ApiCustomException {
        TaxVO result = taxService.getRefundData();
        return ResponseEntity.ok(result);
    }
}
