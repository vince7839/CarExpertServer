package com.carexpert.service;

import com.carexpert.common.CommonType;
import com.carexpert.dao.UserRepository;
import com.carexpert.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    public void save(User user){
        repository.save(user);
    }

    public Page<User> findByPage(int page){
        System.out.println("findByPage:"+page);
        Pageable pageable = PageRequest.of(page,10);
        Page<User> result = repository.findByType(CommonType.USER_TYPE_NORMAL,pageable);
        if (result.getTotalElements() == 0){
            for (int i=0;i<30;i++){
                User u = new User();
                u.setUsername("name_"+i);
                u.setType(CommonType.USER_TYPE_NORMAL);
                save(u);
            }
        }
        return result;
    }

    public User findById(Integer id){
        return repository.getOne(id);
    }
}
