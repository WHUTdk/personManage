package com.dingkai.personManage.business.exception;

import com.dingkai.personManage.common.response.BaseResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @Author dingkai
 * @Date 2020/7/29 23:44
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResult handleGlobalException(Exception e) {
        e.printStackTrace();
        return BaseResult.error("-1", e.getMessage());
    }

    /**
     * 捕获参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        FieldError fieldError = e.getBindingResult().getFieldError();
        return BaseResult.error("-1", fieldError == null ? e.getMessage() : fieldError.getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult handleConstraintViolationException(ConstraintViolationException e) {
        e.printStackTrace();
        return BaseResult.error("-1", e.getMessage());
    }

}
