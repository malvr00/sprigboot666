package com.szs.yongil.common;

import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ApiCustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(ApiCustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
