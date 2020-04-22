package com.carexpert.common;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class PageVO {
    Long count;
    Integer page;
    List data;
    Integer code = 0;
}
