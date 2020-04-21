package com.carexpert.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue
    Integer id;
    @Column(unique = true)
    String username;
    String password;
    String phone;
    Integer type;
}
