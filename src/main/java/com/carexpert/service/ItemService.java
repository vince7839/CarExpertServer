package com.carexpert.service;

import com.carexpert.common.CommonType;
import com.carexpert.common.NodeVO;
import com.carexpert.dao.ItemRepository;
import com.carexpert.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
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
            //items.add(item);
        }
        return items;
    }

    public void addItem(Item item){
        repository.save(item);
    }


    public int getChildLevel(Integer id){
        Item item = repository.getOne(id);
        if (item != null && item.getLevel() != null){
            return item.getLevel() + 1;
        }else {
            return -2;
        }
    }

    public Item findById(Integer id)
    {
        return repository.getOne(id);
    }

    public List<Item> findFile(Integer parent,String type){
        return repository.findByParentAndType(parent,type);
    }

    public void save(Item item){
        repository.save(item);
    }

    //递归删除此目录及其所有子类
    public void deleteRecursively(Integer id){
        List<Item> children = findByParent(id);
        if (children.isEmpty()){
            Item target = repository.getOne(id);
            if (target.getPath() != null){
                File file = new File(target.getPath());
                file.delete();
            }
            repository.deleteById(id);
        }else {
            for(Item child:children) {
                deleteRecursively(child.getId());
            }
        }
    }

    public List<NodeVO> getNodeList(Integer top){
        List<NodeVO> list = new ArrayList<>();
        List<Item> ones = repository.findByParent(top);
        for (Item i:ones){
            NodeVO node = new NodeVO();
            List<Item> children = repository.findByParent(i.getId());
            node.setSelf(i);
            node.setChildren(children);
            list.add(node);
        }
        return list;
    }
}
