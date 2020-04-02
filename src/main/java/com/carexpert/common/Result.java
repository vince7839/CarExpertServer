package com.carexpert.common;

import lombok.Data;

@Data
public class Result {
    int state;
    String msg;
    Object data;

    public static Result fail(int code){
        Result r = new Result();
        r.state = code;
        return r;
    }

    public static Result success(Object data){
        Result r = new Result();
        r.data = data;
        r.state = 200;
        return  r;
    }
}
