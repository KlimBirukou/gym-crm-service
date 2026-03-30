package com.epam.gym.configuration;

import com.epam.gym.configuration.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI gymOpenAPI() {
        return new OpenAPI()
            .info(buildApiInfo())
            .servers(buildServers())
            .components(new Components().addSecuritySchemes("bearerAuth", buildSecurityScheme()))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
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
                .url(swaggerProperties.serverUrl())
                .description(swaggerProperties.serverDescription())
        );
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }
}
