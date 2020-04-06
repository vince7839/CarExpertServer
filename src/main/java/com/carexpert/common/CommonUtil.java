package com.carexpert.common;

import org.springframework.util.StringUtils;

public class CommonUtil {
    public static boolean isOptionRight(String option, Integer answer) {
        Integer builder = 0;
        if (StringUtils.isEmpty(answer)){
            return false;
        }else if ("A".equals(option)) {
            builder = answer & 0x01;
        }else if ("B".equals(option)) {
            builder = answer & 0x10;
        }else if ("C".equals(option)) {
            builder = answer & 0x100;
        }else if ("D".equals(option)) {
            builder = answer & 0x1000;
        }
        return builder != 0;
    }

    public static boolean isModuleEnable(Integer permission,Integer flag){
        if(permission == null || flag == null){
            return false;
        }
        return (permission & (1 << flag)) != 0;
    }
}
