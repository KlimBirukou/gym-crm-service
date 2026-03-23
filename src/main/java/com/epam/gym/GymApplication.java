package com.epam.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

import java.time.Duration;

@SpringBootApplication(
    exclude = UserDetailsServiceAutoConfiguration.class
)
public class GymApplication {

    public static final String DEFAULT_USERNAME_DELIMITER = ".";
    public static final String REQUEST_MDC_KEY = "requestUid";
    public static final String REQUEST_HEADER_NAME = "X-Request-Uid";
    public static final int MAX_LOGIN_ATTEMPTS_COUNT = 3;
    public static final Duration BLOCK_DURATION = Duration.ofMinutes(15);

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
