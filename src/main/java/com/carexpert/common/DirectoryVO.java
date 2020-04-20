package com.carexpert.common;

import lombok.Data;

import java.util.List;

@Data
public class DirectoryVO {
    String title;
    Integer id;
    List<DirectoryVO> children;
}
