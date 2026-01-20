package com.studying.first_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCaching
@RestController
public class FirstSpringAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstSpringAppApplication.class, args);
    }
}
