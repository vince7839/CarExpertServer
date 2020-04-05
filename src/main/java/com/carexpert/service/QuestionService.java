package com.carexpert.service;

import com.carexpert.dao.QuestionRepository;
import com.carexpert.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository repository;

    public void save(Question question){
        repository.save(question);
    }

    public void deleteById(Integer id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public List<Question> findByParent(Integer parent){
        return repository.findByParent(parent);
    }
}
