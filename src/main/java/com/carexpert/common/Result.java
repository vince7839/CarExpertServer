package com.carexpert.common;

import lombok.Data;

@Data
public class Result {
    int state;
    String msg;
    Object data;
    public final static Result SUCCESS = new Result();
    public final static Result FAIL = new Result();

    static {
        SUCCESS.state = 200;
        FAIL.state = 500;
    }

    public static Result fail(int code){
        Result r = new Result();
        r.state = code;
        return r;
    }

    public static Result fail(String msg){
        Result r = new Result();
        r.msg = msg;
        r.state = 500;
        return r;
    }

    public static Result success(Object data){
        Result r = new Result();
        r.data = data;
        r.state = 200;
        return  r;
    }
}
