package com.epam.gym.controller.rest.assignment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AssignRequest(

    @NotBlank(message = "Trainee username is required")
    @Schema(
        description = "Username of trainee",
        example = "Geralt.zRivii"
    )
    String traineeUsername,
    @NotBlank(message = "Trainer username is required")
    @Schema(
        description = "Username of traineк",
        example = "Vesemir.Oldman"
    )
    String trainerUsername
) {
}
