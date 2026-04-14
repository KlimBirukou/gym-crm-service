package com.epam.gym.crm.controller.rest.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

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
