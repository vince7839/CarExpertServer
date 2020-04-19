package com.carexpert.common;

import com.carexpert.exception.CommonException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handle(Exception e){
        System.out.println("handle:"+e.getMessage());
        e.printStackTrace();
        if (e instanceof CommonException){
            return Result.fail(((CommonException) e).getType());
        }
        return Result.fail(e.getMessage());
    }
}
