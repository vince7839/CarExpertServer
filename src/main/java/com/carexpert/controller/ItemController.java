package com.carexpert.controller;

import com.carexpert.common.ItemConstant;
import com.carexpert.common.Result;
import com.carexpert.entity.Item;
import com.carexpert.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/home")
    public ModelAndView home(ModelAndView mv,Integer parent){
        if (parent == null){
            parent = ItemConstant.NO_PARENT;
            mv.addObject("level",ItemConstant.DIRECTORY_LEVEL_TOP);
        }
        List<Item> items = itemService.findByParent(parent);
        mv.addObject("items",items);
        mv.addObject("parent",parent);
        mv.setViewName("/home");
        return mv;
    }

    @RequestMapping("/inherit/{parent}")
    public Result items(@PathVariable Integer parent){
        return Result.success(itemService.findByParent(parent));
    }
}
