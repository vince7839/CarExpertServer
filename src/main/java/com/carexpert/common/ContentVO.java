package com.carexpert.common;

import com.carexpert.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class ContentVO {
    List<FileVO> document;
    List<FileVO> video;
    List<FileVO> image;
    List<Question> exam;
}
