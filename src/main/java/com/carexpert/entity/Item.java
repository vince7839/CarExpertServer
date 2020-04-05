package com.carexpert.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue
    Integer id;
    Integer level;
    Integer parent;
    Integer priority;
    String type;
    String name;
    String path;
}
