package com.sdydj.moisturizing.product.exception;


import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sdydj.common.exception.BizCodeEnume;
import com.sdydj.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.sdydj.moisturizing.product.controller")
public class MoisturizingExceptionControllerAdvice {
//MethodArgumentNotValidException

        @ExceptionHandler(value = MethodArgumentNotValidException.class)
        public R handleValidException(MethodArgumentNotValidException e){
                log.error("数据出现问题:{}.异常类型:{}",e.getMessage(),e.getClass());
                BindingResult bindingResult = e.getBindingResult();
                Map<String,String> errormap=new HashMap<>();
                bindingResult.getFieldErrors().forEach((fieldError)->{
                        errormap.put(fieldError.getField(),fieldError.getDefaultMessage());
                });

                return R.error(BizCodeEnume.VAIlD_EXCEPTION.getCode(),BizCodeEnume.VAIlD_EXCEPTION.getMsg()).put("data",errormap);
        }


        @ExceptionHandler(value= Throwable.class)
        public  R hadleException(Throwable throwable){
                log.error("错误：",throwable);
                return  R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());

        }
}
