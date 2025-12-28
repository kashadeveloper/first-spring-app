package com.studying.first_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FirstSpringAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstSpringAppApplication.class, args);
    }

    @GetMapping("/")
    public static ResponseEntity<?> index() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
}
