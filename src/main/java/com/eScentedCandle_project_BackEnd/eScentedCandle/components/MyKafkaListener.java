package com.eScentedCandle_project_BackEnd.eScentedCandle.components;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Category;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@KafkaListener(id = "groupA", topics = {"get-all-categories", "insert-a-category"})
public class MyKafkaListener {
//    @KafkaHandler
//    public void listenCategory(Category category) {
//        System.out.println("Received: " + category);
//    }
//
//    @KafkaHandler(isDefault = true)
//    public void unknown(Object object) {
//        System.out.println("Received unknown: " + object);
//    }
//
//    @KafkaHandler
//    public void listenListOfCategories(List<Category> categories) {
//        System.out.println("Received: " + categories);
//    }
}
