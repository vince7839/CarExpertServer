package com.carexpert.controller;

import com.carexpert.common.*;
import com.carexpert.entity.User;
import com.carexpert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    CacheManager cacheManager;

    @RequestMapping("/user")
    public String user(ModelAndView mv, Integer page) {

        return "user2";
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

    @RequestMapping("/user/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Integer id) {
        service.deleteById(id);
        return Result.SUCCESS;
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
        System.out.println("permission:"+temp);
        if (user.getId() == null){
            service.add(user);
        }else{
            service.update(user);
        }
        return Result.success(null);
    }



   // @RequestMapping("/user/{id}")
    public ModelAndView info(@PathVariable Integer id, ModelAndView mv) {
        User user = service.findById(id);
        mv.addObject("user", user);
        mv.addObject("util", new CommonUtil());
        mv.setViewName("info");
        return mv;
    }

    @RequestMapping("/user/data")
    public ModelAndView data(Integer id, ModelAndView mv) {
        User user = new User();
        if(id != null) {
            user = service.findById(id);
        }
        mv.addObject("user", user);
        mv.addObject("util", new CommonUtil());
        mv.setViewName("user_data");
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
    public Result reset(Integer id, @NotNull String code,@NotNull Integer type, String data) throws Exception {
        System.out.println("reset: id:"+id+" code:"+code+" type:"+type+" data:"+data);
        User user = service.findById(id);
        String cacheCode = cacheManager.getCache("vcode").get(id,String.class);
        System.out.println("cache vcode:"+cacheCode);
        if (user == null){
            return Result.fail("no such user");
        }else if(!code.equals(cacheCode)){
            return Result.fail("code is wrong");
        } else if (type.equals(CommonType.RESET_TYPE_PHONE)){
            user.setPhone(data);
        }else if(type.equals(CommonType.RESET_TYPE_PASSWORD) ){
            user.setPassword(data);
        }
        cacheManager.getCache("vcode").evict(id);
        service.update(user);
        return Result.success(null);
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
        User user = service.findById(id);
        if (user != null){
            int  r = new Random().nextInt(9000) + 1000;
            String code = r + "";
            String phone = user.getPhone();

            //send code
            System.out.println("generate vcode:"+code);
            cacheManager.getCache("vcode").put(id,code);
            return Result.success(null);
        }else {
            return Result.fail("no such user");
        }
    }

    @RequestMapping("/user2")
    public String user2(){
        return "user2";
    }

    @RequestMapping("/user/list")
    @ResponseBody
    public PageVO list(Integer page,Integer limit) throws Exception {
        Page<User> result = service.findByPage(page-1);
        PageVO vo = new PageVO();
        vo.setCount(result.getTotalElements());
        vo.setPage(page);
        vo.setData(result.getContent());
        return vo;
    }

    @RequestMapping("/user/search")
    @ResponseBody
    public Result search(User user){
        Specification<User> sp = new Specification<User>() {
            List<Predicate> predicates = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (!StringUtils.isEmpty(user.getUsername())){
                    Predicate predicate = cb.like(root.get("username").as(String.class),"%"+user.getUsername()+"%");
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(user.getName())){
                    Predicate predicate = cb.like(root.get("name").as(String.class),"%"+user.getName()+"%");
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(user.getPhone())){
                    Predicate predicate = cb.like(root.get("phone").as(String.class),"%"+user.getPhone()+"%");
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(user.getEmail())){
                    Predicate predicate = cb.like(root.get("email").as(String.class),"%"+user.getEmail()+"%");
                    predicates.add(predicate);
                }
                Predicate[] arr = new Predicate[predicates.size()];
                cq.where(predicates.toArray(arr));
                return cq.getRestriction();
            }
        };
        List<User> list = service.search(sp);
        return Result.success(list);
    }
}
