package com.carexpert.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue
    Integer id;
    Integer parent;
    String title;
    String a;
    String b;
    String c;
    String d;
    Integer answer;
    String reason;
    Integer sortIndex;
}
