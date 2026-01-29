package com.epam.gym.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperConfiguration {

    @Bean("fromFileParsingObjectMapper")
    public ObjectMapper fromFileParsingObjectMapper() {
        return new ObjectMapper();
    }
}
