package com.carexpert.service;

import com.carexpert.dao.UserRepository;
import com.carexpert.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    public void save(User user){
        repository.save(user);
    }
}
