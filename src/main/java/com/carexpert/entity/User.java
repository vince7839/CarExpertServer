package com.carexpert.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    String password;
    @Column(unique = true)
    String phone;
    Integer type;
    Integer permission;
    String name;
    String email;
    String sex;
    String address;
    String school;
    String major;
    String note;
    Integer state;
}

