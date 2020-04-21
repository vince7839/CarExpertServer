package com.carexpert.service;

import com.carexpert.common.CommonType;
import com.carexpert.dao.AdminRepository;
import com.carexpert.dao.UserRepository;
import com.carexpert.entity.Admin;
import com.carexpert.entity.User;
import com.carexpert.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AdminService {
    @Autowired
    AdminRepository repository;

    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public void add(Admin admin) throws Exception {
        if (findByUsername(admin.getUsername()) != null){
            throw CommonException.EXCEPTION_USERNAME_EXIST;
        }
        admin.setType(CommonType.ADMIN_TYPE_NORMAL);
        repository.save(admin);
    }

    public void update(Admin admin) throws Exception {
        Admin u = findByUsername(admin.getUsername());
        if ( u != null && !u.getId().equals(admin.getId())){
            throw CommonException.EXCEPTION_USERNAME_EXIST;
        }
        repository.save(admin);
    }

    public Page<Admin> findByPage(int page) throws Exception {
        System.out.println("findByPage:"+page);
        Pageable pageable = PageRequest.of(page,10);
        Page<Admin> result = repository.findAll(pageable);
        //mock data
        return result;
    }

    public Admin findById(Integer id){
        return repository.getOne(id);
    }

    public Admin findByUsername(String username){
        return repository.findByUsername(username);
    }

    @PostConstruct
    public void init(){
        if (repository.count() == 0){
            Admin sa = new Admin();
            sa.setUsername("sa");
            sa.setPassword("3377501");
            sa.setType(CommonType.ADMIN_TYPE_SUPER);
            repository.save(sa);
        }
    }
}
