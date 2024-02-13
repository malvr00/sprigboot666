package com.szs.yongil.util.exception;

import com.szs.yongil.util.exception.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiCustomException extends Exception {
    private ErrorEnum errorCode;

}
