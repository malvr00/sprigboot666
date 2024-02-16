package com.szs.yongil.controller.member;

import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.service.client.ClientService;
import com.szs.yongil.service.member.MemberService;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.util.exception.enums.ErrorEnum;
import com.szs.yongil.util.member.MemberUtil;
import com.szs.yongil.vo.member.MemberSignVO;
import com.szs.yongil.vo.scrap.ScrapVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
@Tag(name = "유저 POST 컨트롤러", description = "회원가입/스크랩 유저관련 API 입니다.")
public class MemberPostController {

    private final MemberService memberService;
    private final ClientService clientService;
    private final MemberUtil memberUtil;

    @PostMapping("/signup")
    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입하는 API 입니다.")
    public ResponseEntity<MemberSignVO> signup(
            @Valid @RequestBody MemberSignDto memberSignDto
    ) throws ApiCustomException {

        if (!memberUtil.userValidationCheck(memberSignDto.getName(), memberSignDto.getRegNo())) {
            throw new ApiCustomException(ErrorEnum.USER_INFO_00);
        }
        if (memberUtil.userCheck(memberSignDto.getName(), memberSignDto.getRegNo())) {
            throw new ApiCustomException(ErrorEnum.USER_INFO_01);
        }
        if (memberUtil.userIdDuplicateCheck(memberSignDto.getUserId())) {
            throw new ApiCustomException(ErrorEnum.USER_INFO_02);
        }

        MemberSignVO result = memberService.signup(memberSignDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/scrap")
    @Operation(summary = "SZS 스크랩", description = "사용자 스크래핑 API 입니다.")
    public ResponseEntity<ScrapVO> scrap() throws ApiCustomException {
        ScrapVO result = clientService.getScrapData();
        return ResponseEntity.ok(result);
    }
}
