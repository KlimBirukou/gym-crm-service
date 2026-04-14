package com.epam.gym.crm.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application.security.endpoints")
public record SecurityProperties(
    List<String> publicEndpoints
) {

}
