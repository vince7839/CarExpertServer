package com.carexpert.dao;

import com.carexpert.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Page<User> findAll(Pageable pageable);
    User findByUsername(String username);
    List<User> findAll(Specification sp);
}
