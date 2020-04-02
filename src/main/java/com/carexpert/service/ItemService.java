package com.carexpert.service;

import com.carexpert.dao.ItemRepository;
import com.carexpert.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    ItemRepository repository;
    public List<Item> findByParent(Integer parent){
        List<Item> items = repository.findByParent(parent);
        for(int i=0;i<5;i++){
            Item item = new Item();
            item.setName("hhhhhhhhhhhdhdghdghdghd"+i);
            item.setParent(parent);
            item.setLevel(0);
            items.add(item);
        }
        return items;
    }
}
