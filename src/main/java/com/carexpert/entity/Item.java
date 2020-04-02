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
    int id;
    int level;
    int parent;
    int priority;
    String type;
    String name;
    String path;
}
