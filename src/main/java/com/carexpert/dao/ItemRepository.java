package com.carexpert.dao;

import com.carexpert.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    List<Item> findByParent(Integer parent);
    List<Item> findByParentAndType(Integer parent,String type);
    List<Item> findByLevel(Integer level);
    Item findByFlag(Integer flag);
    Page<Item> findByParentAndType(Pageable pageable,Integer parent, String type);
}
