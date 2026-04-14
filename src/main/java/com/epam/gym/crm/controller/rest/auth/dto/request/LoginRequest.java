package com.epam.gym.crm.controller.rest.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LoginRequest(

    @NotBlank(message = "Username is required")
    @Schema(
        description = "Username of user",
        example = "Vesemir.Oldman"
    )
    String username,

    @NotBlank(message = "Password is required")
    @Schema(
        description = "Current password of user",
        example = "password8"
    )
    String password
) {

}
