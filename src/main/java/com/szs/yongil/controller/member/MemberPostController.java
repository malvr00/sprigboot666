package com.szs.yongil.controller.member;

import com.szs.yongil.dto.member.MemberLoginDto;
import com.szs.yongil.dto.member.MemberSignDto;
import com.szs.yongil.service.member.MemberServiceImpl;
import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.util.exception.enums.ErrorEnum;
import com.szs.yongil.util.member.MemberUtil;
import com.szs.yongil.vo.member.MemberSignVO;
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
public class MemberPostController {

    private final MemberServiceImpl memberServiceImpl;
    private final MemberUtil memberUtil;

    @PostMapping("/signup")
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

        MemberSignVO result = memberServiceImpl.signup(memberSignDto);
        return ResponseEntity.ok(result);
    }
}
