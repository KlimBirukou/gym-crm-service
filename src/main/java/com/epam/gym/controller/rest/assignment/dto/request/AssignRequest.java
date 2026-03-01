package com.epam.gym.controller.rest.assignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AssignRequest(

    @NotBlank(message = "Trainee username is required")
    String traineeUsername,
    @NotBlank(message = "Trainer username is required")
    String trainerUsername
) {
}
