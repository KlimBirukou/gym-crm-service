package com.epam.gym.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI gymOpenAPI() {
        return new OpenAPI()
            .info(buildApiInfo())
            .servers(buildServers());
    }

    private Info buildApiInfo() {
        return new Info()
            .title("Gym CRM API")
            .description("REST-API description with examples")
            .version("1.0.0");
    }

    private List<Server> buildServers() {
        return List.of(
            new Server()
                .url("http://localhost:8080")
                .description("Local development server")
        );
    }
}
