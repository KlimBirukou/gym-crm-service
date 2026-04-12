package com.epam.gym.crm.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.swagger")
public record SwaggerProperties(
    String serverUrl,
    String serverDescription
) {

}
