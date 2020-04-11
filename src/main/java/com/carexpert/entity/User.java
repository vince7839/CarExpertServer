package com.carexpert.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    Integer id;
    @Column(unique = true)
    String username;
    String password;
    @Column(unique = true)
    String phone;
    Integer type;
    Integer permission;
}

