package com.carexpert.controller;

import com.carexpert.common.CommonUtil;
import com.carexpert.common.Result;
import com.carexpert.entity.Question;
import com.carexpert.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class ExamController {

    @Autowired
    QuestionService service;

    @RequestMapping("/exam")
    public ModelAndView exam(ModelAndView mv,Integer parent){
        List<Question> questions = service.findByParent(parent);
        mv.addObject("questions",questions);
        mv.addObject("parent",parent);
        mv.addObject("comparator",new CommonUtil());
        mv.setViewName("exam");
        return mv;
    }

    @RequestMapping("/question/save")
    public String add(Question question,String[] answers){
        System.out.println(question+" answers:"+ Arrays.toString(answers));
        if (answers != null){
            Integer builder = 0;
            for (String item:answers) {
                if ("A".equals(item)){
                    builder = builder | 0x01;
                }
                if ("B".equals(item)){
                    builder = builder | 0x10;
                }
                if ("C".equals(item)){
                    builder = builder | 0x100;
                }
                if ("D".equals(item)){
                    builder = builder | 0x1000;
                }
            }
            System.out.println("answer:"+builder);
            question.setAnswer(builder);
        }
        service.save(question);
        return "redirect:/exam?parent="+question.getParent();
    }

    @RequestMapping("/question/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Integer id){
        System.out.println("delete question:"+id);
        service.deleteById(id);
        return Result.success(null);
    }
}
