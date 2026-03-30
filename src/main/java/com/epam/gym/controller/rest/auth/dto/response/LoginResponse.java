package com.epam.gym.controller.rest.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LoginResponse(

    @Schema(
        description = "JWT access token",
        example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    @NonNull String accessToken,

    @Schema(
        description = "Token type",
        example = "Bearer"
    )
    @NonNull String tokenType,

    @Schema(
        description = "Token expiration time in seconds",
        example = "86400"
    )
    long expiresIn
) {

}
