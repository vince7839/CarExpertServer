package com.carexpert.dao;

import com.carexpert.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    public List<Item> findByParent(Integer parent);
    public List<Item> findByParentAndType(Integer parent,String type);
}
