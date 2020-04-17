package com.carexpert.common;

import lombok.Data;

@Data
public class FileVO {
    String type;
    String name;
    String tag;
    String size;
    String url;
    String cover;
    Integer heat;
}
