package com.epam.gym.crm.controller.rest.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RegisterTrainerRequest(

    @NotBlank(message = "First name is required")
    @Schema(
        description = "First name of potential trainer",
        example = "Vesemir"
    )
    String firstName,

    @NotBlank(message = "Last name is required")
    @Schema(
        description = "Last name of potential trainee",
        example = "Oldman"
    )
    String lastName,

    @NotBlank(message = "Specialization is required")
    @Schema(
        description = "The name of the specialization existing in the system",
        example = "Martial Arts"
    )
    String specialization
) {

}
