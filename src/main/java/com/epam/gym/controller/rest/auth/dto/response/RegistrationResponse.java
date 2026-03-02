package com.epam.gym.controller.rest.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Builder

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RegistrationResponse(

    @Schema(
        description = "New user username",
        example = "Vesemir.Oldman"
    )
    @NonNull String username,
    @Schema(
        description = "New user generated password",
        example = "sfmp1239d8"
    )
    @NonNull String password
) {
}
