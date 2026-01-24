package com.epam.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymApplication {

    public static final String DEFAULT_USERNAME_DELIMITER = "_";

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
