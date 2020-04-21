package com.carexpert.service;

import com.carexpert.common.CommonType;
import com.carexpert.dao.UserRepository;
import com.carexpert.entity.User;
import com.carexpert.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    public void add(User user) throws Exception {
        if (findByUsername(user.getUsername()) != null){
            throw CommonException.EXCEPTION_USERNAME_EXIST;
        }
        user.setType(CommonType.USER_TYPE_NORMAL);
        repository.save(user);
    }

    public void update(User user) throws Exception {
        User u = findByUsername(user.getUsername());
        if ( u != null && !u.getId().equals(user.getId())){
            throw CommonException.EXCEPTION_USERNAME_EXIST;
        }
        repository.save(user);
    }

    public Page<User> findByPage(int page) throws Exception {
        System.out.println("findByPage:"+page);
        Pageable pageable = PageRequest.of(page,10);
        Page<User> result = repository.findByType(CommonType.USER_TYPE_NORMAL,pageable);
        //mock data
        if (result.getTotalElements() == 0){
            for (int i=0;i<30;i++){
                User u = new User();
                u.setUsername("name_"+i);
                u.setType(CommonType.USER_TYPE_NORMAL);
                add(u);
            }
        }
        return result;
    }

    public User findById(Integer id){
        return repository.getOne(id);
    }

    public User findByUsername(String username){
        return repository.findByUsername(username);
    }

    public void deleteById(Integer id){
        repository.deleteById(id);
    }
}
