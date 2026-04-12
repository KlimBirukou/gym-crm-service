package com.epam.gym.crm.controller.rest.trainer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateTrainerRequest(

    @NotBlank(message = "Trainer first name is required")
    @Schema(
        description = "New trainer first name",
        example = "Vesemir"
    )
    String firstName,

    @NotBlank(message = "Trainer last name is required")
    @Schema(
        description = "New trainer last name",
        example = "Oldman"
    )
    String lastName,

    @NotBlank(message = "Specialization is required")
    @Schema(
        description = "New trainee specialization, existed in the system",
        example = "2001-01-01"
    )
    String specializationName
) {

}
