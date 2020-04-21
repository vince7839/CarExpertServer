package com.carexpert.dao;

import com.carexpert.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Page<Admin> findAll(Pageable pageable);
    Admin findByUsername(String username);
}
