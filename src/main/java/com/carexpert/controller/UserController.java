package com.carexpert.controller;

import com.carexpert.common.CommonUtil;
import com.carexpert.common.PageVO;
import com.carexpert.common.Result;
import com.carexpert.common.UserVo;
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
    public String user(ModelAndView mv, Integer page) {

        return "user";
    }

    @RequestMapping("/user/page/{page}")
    @ResponseBody
    public PageVO page(@PathVariable Integer page) {
        Page<User> result = service.findByPage(page);
        System.out.println("" + result.getNumber() + " " + result.getNumberOfElements());
        PageVO vo = new PageVO();
        vo.setCount(result.getTotalElements());
        vo.setPage(page);
        vo.setData(result.getContent());
        return vo;
    }

    @RequestMapping("/user/save")
    @ResponseBody
    public Result add(User user, int[] module) {
        System.out.println("user save" + user + " " + Arrays.toString(module));
        int temp = 0;
        if (module != null) {
            for (int i : module) {
                temp |= (1 << i);
            }
        }
        System.out.println(temp);
        user.setPermission(temp);
        service.save(user);
        return Result.success(null);
    }

    @RequestMapping("/user/{id}")
    public ModelAndView add(@PathVariable Integer id, ModelAndView mv) {
        User user = service.findById(id);
        mv.addObject("user", user);
        mv.addObject("util", new CommonUtil());
        mv.setViewName("info");
        return mv;
    }

    @RequestMapping("/user/login")
    public Result login(String username,String password){
        User user = service.login(username, password);
        if (user == null){
            return Result.fail("wrong username or password");
        }else {
            UserVo vo = new UserVo();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setPermission(user.getPermission());
            return Result.success(vo);
        }
    }

    @RequestMapping("/user/reset")
    public Result reset(Integer id,String password){
        User user = service.findById(id);
        if (user != null){
            user.setPassword(password);
            service.save(user);
            return Result.success(null);
        }else {
            return Result.fail("no such user");
        }
    }

    @RequestMapping("/user/phone")
    public Result phone(Integer id,String phone){
        User user = service.findById(id);
        if (user != null){
            user.setPhone(phone);
            service.save(user);
            return Result.success(null);
        }else {
            return Result.fail("no such user");
        }
    }

    @RequestMapping("/code/{id}")
    public Result phone(@PathVariable Integer id){
        String code = "";
        User user = service.findById(id);
        if (user != null){
            String phone = user.getPhone();
            //send code
            //add to cache
            return Result.success(null);
        }else {
            return Result.fail("no such user");
        }
    }
}
