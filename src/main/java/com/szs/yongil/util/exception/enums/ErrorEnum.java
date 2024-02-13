package com.szs.yongil.util.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    /*
        USER_XXX
        유저관련 에러
     */
    USER_INFO_00(HttpStatus.UNAUTHORIZED, "가입이 불가능한 사용자 입니다."),
    USER_INFO_01(HttpStatus.BAD_REQUEST, "이미 가입되어 있는 유저 입니다."),
    USER_INFO_02(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
