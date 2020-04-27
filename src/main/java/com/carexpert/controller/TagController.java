package com.carexpert.controller;

import com.carexpert.common.Result;
import com.carexpert.dao.TagRepository;
import com.carexpert.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TagController {

    @Autowired
    TagRepository tagRepository;

    @RequestMapping("/tag/add")
    @ResponseBody
    public Result add(String name,String type){
        System.out.println("add tag："+name);
        if(tagRepository.existsByName(name)){
            return Result.fail(name+" 已经存在");
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setType(type);
        tagRepository.save(tag);
        return Result.SUCCESS;
    }

    @RequestMapping("/tag/delete/{id}")
    @ResponseBody
    public Result add(@PathVariable Integer id){
        System.out.println("delete tag："+id);
        Tag tag = tagRepository.getOne(id);
        if (tag == null){
            return Result.fail("no such tag");
        }
        tagRepository.deleteById(id);
        return Result.SUCCESS;
    }

    @RequestMapping("/tag/list")
    @ResponseBody
    public Result list(String type){
        List<Tag> tags = tagRepository.findByType(type);
        return Result.success(tags);
    }

    @RequestMapping("/tag")
    public ModelAndView all(ModelAndView mv){
        List<Tag> tags = tagRepository.findAll();
        mv.addObject("tags",tags);
        mv.setViewName("tag");
        return mv;
    }
}
