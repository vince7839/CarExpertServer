package com.carexpert.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue
    Integer id;
    Integer level;//表示此item是几级目录
    Integer parent;//它的父目录的id
    Integer priority;//以备排序使用
    String type;//如果是文件 那么它的文件类型
    String name;
    String filename;//文件保存在服务器的路径
    @Column(unique = true)
    Integer flag;//表示在权限int数中占第几个bit
    String tag;
    Integer heat;
    String cover;
}
