package com.carexpert.controller;

import com.carexpert.common.CommonType;
import com.carexpert.common.PageVO;
import com.carexpert.common.Result;
import com.carexpert.dao.AdminRepository;
import com.carexpert.entity.Admin;
import com.carexpert.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AdminService service;

    @RequestMapping("/signin")
    @ResponseBody
    public Result signin(String username, String password, HttpSession session){
        System.out.println("admin login:"+username+" "+password);
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && password.equals(admin.getPassword())){
            session.setAttribute("admin",admin);
            return Result.SUCCESS;
        }
        return Result.FAIL;
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin_list";
    }

    @RequestMapping("/admin/add")
    public String add(){
        return "add_admin";
    }

    @RequestMapping("/admin/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Integer id) {
        service.deleteById(id);
        return Result.SUCCESS;
    }

    @RequestMapping("/manager")
    public ModelAndView manage(HttpSession session,ModelAndView mv){
        Admin admin = (Admin)session.getAttribute("admin");
        if (admin == null){
            mv.setViewName("login");
        }else {
            if(admin.getType()!= null && admin.getType().equals(CommonType.ADMIN_TYPE_SUPER)){
                mv.setViewName("manager");
            }else{
                mv.setViewName("error");
                mv.addObject("error","您没有权限，只有超级管理员可以访问此页");
            }
        }
        return mv;
    }

    @RequestMapping("/admin/save")
    @ResponseBody
    public Result save(Admin admin){
        System.out.println("admin save:"+admin);
        adminRepository.save(admin);
        return Result.SUCCESS;
    }

    @RequestMapping("/admin/page/{page}")
    @ResponseBody
    public PageVO page(@PathVariable Integer page) throws Exception {
        Page<Admin> result = service.findByPage(page);
        PageVO vo = new PageVO();
        vo.setCount(result.getTotalElements());
        vo.setPage(page);
        vo.setData(result.getContent());
        return vo;
    }

    @RequestMapping("/admin/{id}")
    public ModelAndView info(@PathVariable Integer id, ModelAndView mv) {
        Admin admin = service.findById(id);
        mv.addObject("admin", admin);
        mv.setViewName("admin_info");
        return mv;
    }

}
