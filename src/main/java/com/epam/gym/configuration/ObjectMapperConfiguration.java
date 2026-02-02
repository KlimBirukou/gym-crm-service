package com.epam.gym.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class ObjectMapperConfiguration {

    @Bean("fromFileParsingObjectMapper")
    public ObjectMapper fromFileParsingObjectMapper() {
        return JsonMapper.builder()
            .findAndAddModules()
            .build();
    }
}
