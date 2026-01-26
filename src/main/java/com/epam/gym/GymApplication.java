package com.epam.gym;

import com.epam.gym.facade.IGymFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymApplication {

    public static final String DEFAULT_USERNAME_DELIMITER = ".";

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
