package com.epam.gym.controller.rest.trainee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TrainerProfileResponse(

    @Schema(
        description = "Trainer username",
        example = "Vesemi.Oldman"
    )
    @NonNull String username,
    @Schema(
        description = "Trainer first name",
        example = "Vesemir"
    )
    @NonNull String firstName,
    @Schema(
        description = "Trainer last name",
        example = "Oldman"
    )
    @NonNull String lastName,
    @Schema(
        description = "Trainer specialization name",
        example = "Martial Arts"
    )
    @NonNull String specializationName
) {
}
