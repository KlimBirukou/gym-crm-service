package com.epam.gym.controller.rest.trainer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateTrainerRequest(

    @NotBlank(message = "Trainer first name is required")
    String firstName,
    @NotBlank(message = "Trainer last name is required")
    String lastName,
    @NotBlank(message = "Specialization is required")
    String specializationName
) {
}
