package com.carexpert.service;

import com.carexpert.common.CommonType;
import com.carexpert.common.CommonUtil;
import com.carexpert.common.FileVO;
import com.carexpert.common.NodeVO;
import com.carexpert.dao.ItemRepository;
import com.carexpert.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Item findByFlag(Integer moduleFlag){
        return repository.findByFlag(moduleFlag);
    }

    public List<Item> findFile(Integer parent,String type){
        return repository.findByParentAndType(parent,type);
    }

    public void save(Item item){
        repository.save(item);
    }

    //递归删除此目录及其所有子类
    public void deleteRecursively(Integer id){
        Item target = repository.getOne(id);
        List<Item> children = findByParent(id);
        if (!children.isEmpty()){
            for(Item child:children) {
                deleteRecursively(child.getId());
            }
        }
        if (!StringUtils.isEmpty(target.getFilename())){
            CommonUtil.deleteFile(target);
        }
        repository.deleteById(id);
    }

    public List<NodeVO> getNodeList(Integer moduleFlag){
        Item module = repository.findByFlag(moduleFlag);
        if (module == null){
            return null;
        }
        List<NodeVO> list = new ArrayList<>();
        List<Item> ones = repository.findByParent(module.getId());
        for (Item i:ones){
            NodeVO node = new NodeVO();
            List<Item> children = repository.findByParent(i.getId());
            node.setSelf(i);
            node.setChildren(children);
            list.add(node);
        }
        return list;
    }

    public List<FileVO> getFileList(Integer parent,String type){
        List<Item> items = findFile(parent,type);
        List<FileVO> list = new ArrayList<>();
        for(Item item:items){
            FileVO f = new FileVO();
            f.setName(item.getName());
            f.setTag(item.getTag());
            if(StringUtils.isEmpty(f.getTag())){
                f.setTag("基础");
            }
            f.setHeat(item.getHeat());
            if(f.getHeat() == null){
                f.setHeat(100);
            }
            f.setUrl(CommonUtil.getFileUrl(item));
            f.setCover(CommonUtil.getCoverUrl(item.getCover()));
            f.setType(item.getType());
            list.add(f);
        }
        return list;
    }

    @PostConstruct
    public void init(){
        System.out.println("init");
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"汽车电工电子");
        map.put(2,"汽车使用与维护");
        map.put(3,"发动机构造与检修");
        map.put(4,"变速器构造与检修");
        map.put(5,"汽车动力底盘");
        map.put(6,"汽车电气空调");
        map.put(7,"汽车检测与故障诊断");
        map.put(8,"新能源汽车整车");
        map.put(9,"新能源汽车拓展");
        map.put(10,"新能源汽车基础");
        map.put(11,"新能源汽车电能与管理");
        map.put(12,"新能源汽车电机与控制");
        map.put(13,"新能源汽车电气");
        for(int i=1;i<=13;i++){
            Item check = repository.findByFlag(i);
            if (check != null){
                continue;
            }
            Item item = new Item();
            item.setFlag(i);
            item.setLevel(CommonType.ITEM_LEVEL_TOP);
            item.setParent(CommonType.NO_PARENT);
            item.setName(map.get(i));
            addItem(item);
        }
    }
}
