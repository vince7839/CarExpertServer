package com.carexpert.exception;

import lombok.Data;

@Data
public class CommonException extends Exception {
    public static final int EXCEPTION_TYPE_USERNAME_EXIST = 501;
    int type;

    public CommonException(int type){
        this.type = type;
    }

    public static final CommonException EXCEPTION_USERNAME_EXIST = new CommonException(0);
}
