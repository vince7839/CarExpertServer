package com.carexpert.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Tag {
    @Id
    @GeneratedValue
    Integer id;
    String name;
    String type;
}
