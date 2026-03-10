package com.epam.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymApplication {

    public static final String DEFAULT_USERNAME_DELIMITER = ".";
    public static final String REQUEST_MDC_KEY = "requestUid";
    public static final String REQUEST_HEADER_NAME = "X-Request-Uid";

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
