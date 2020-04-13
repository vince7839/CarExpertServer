package com.carexpert.controller;

import com.carexpert.common.CommonUtil;
import com.carexpert.common.PageVO;
import com.carexpert.common.Result;
import com.carexpert.common.UserVO;
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

    @RequestMapping("/user/addnew")
    public String add() {

        return "add_user";
    }

    @RequestMapping("/user/page/{page}")
    @ResponseBody
    public PageVO page(@PathVariable Integer page) throws Exception {
        Page<User> result = service.findByPage(page);
        System.out.println("getNumber:" + result.getNumber()
                + " getNumberOfElements:" + result.getNumberOfElements()
        + " getTotalElements:" + result.getTotalElements());
        PageVO vo = new PageVO();
        vo.setCount(result.getTotalElements());
        vo.setPage(page);
        vo.setData(result.getContent());
        return vo;
    }

    @RequestMapping("/user/save")
    @ResponseBody
    public Result save(User user, int[] module) throws Exception {
        System.out.println("user save" + user + " " + Arrays.toString(module));
        int temp = 0;
        if (module != null) {
            for (int i : module) {
                temp |= (1 << i);
            }
        }
        user.setPermission(temp);
        if (user.getId() == null){
            service.add(user);
        }else{
            service.update(user);
        }
        return Result.success(null);
    }



    @RequestMapping("/user/{id}")
    public ModelAndView info(@PathVariable Integer id, ModelAndView mv) {
        User user = service.findById(id);
        mv.addObject("user", user);
        mv.addObject("util", new CommonUtil());
        mv.setViewName("info");
        return mv;
    }

    @RequestMapping("/user/login")
    @ResponseBody
    public Result login(String username,String password){
        System.out.println("login:"+username+" "+password);
        User user = service.findByUsername(username);
        if (user == null || !password.equals(user.getPassword())){
            return Result.fail("wrong username or password");
        }else {
            UserVO vo = new UserVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setPermission(user.getPermission());
            return Result.success(vo);
        }
    }

    @RequestMapping("/user/reset")
    @ResponseBody
    public Result reset(Integer id,String password) throws Exception {
        User user = service.findById(id);
        if (user != null){
            user.setPassword(password);
            service.update(user);
            return Result.success(null);
        }else {
            return Result.fail("no such user");
        }
    }

    @RequestMapping("/user/phone")
    @ResponseBody
    public Result phone(Integer id,String phone) throws Exception {
        User user = service.findById(id);
        if (user != null){
            user.setPhone(phone);
            service.update(user);
            return Result.success(null);
        }else {
            return Result.fail("no such user");
        }
    }

    @RequestMapping("/code/{id}")
    @ResponseBody
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
