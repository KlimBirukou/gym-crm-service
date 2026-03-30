package com.epam.gym.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.request-uid")
public record RequestUidProperties(
    String headerName,
    String mdcKey
) {

}
