package com.epam.gym.crm.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.user")
public record UserProperties(
    String usernameDelimiter
) {

}
