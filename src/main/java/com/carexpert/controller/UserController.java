package com.carexpert.controller;

import com.carexpert.common.PageVO;
import com.carexpert.common.Result;
import com.carexpert.dao.UserRepository;
import com.carexpert.entity.User;
import com.carexpert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
public class UserController {

    @Autowired
    UserService service;

    @RequestMapping("/user")
    public String user(ModelAndView mv,Integer page){

        return "user";
    }

    @RequestMapping("/user/page/{page}")
    @ResponseBody
    public PageVO page(@PathVariable Integer page){
        Page<User> result = service.findByPage(page);
        System.out.println(""+result.getNumber()+" "+result.getNumberOfElements());
        PageVO vo = new PageVO();
        vo.setCount(result.getTotalElements());
        vo.setPage(page);
        vo.setData(result.getContent());
        return vo;
    }

    @RequestMapping("/user/save")
    @ResponseBody
    public Result add(User user,int[] module){
        System.out.println("user save"+user+" "+ Arrays.toString(module));
        int temp = 0;
        for (int i: module) {
            temp |= (1 << i);
        }
        System.out.println(temp);
        user.setPermission(temp);
        service.save(user);
        return Result.success(null);
    }

    @RequestMapping("/user/{id}")
    @ResponseBody
    public ModelAndView add(@PathVariable Integer id,ModelAndView mv){
        User user = service.findById(id);
        mv.addObject("user",user);
        mv.setViewName("info");
        return mv;
    }
}
