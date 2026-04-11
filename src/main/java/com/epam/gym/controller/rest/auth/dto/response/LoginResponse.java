package com.epam.gym.controller.rest.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

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
