package com.carexpert.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    Integer id;
    String username;
    String password;
    String phone;
    Integer type;
    Integer permission;
}

