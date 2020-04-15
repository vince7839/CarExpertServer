package com.carexpert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CarexpertApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarexpertApplication.class, args);
    }

}
