package com.epam.gym.controlller.rest.training.dto.req;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record CreateTrainingRequest(

    @NotBlank(message = "Trainee username is required")
    String traineeUsername,

    @NotBlank(message = "Trainer username is required")
    String trainerUsername,

    @NotBlank(message = "Training name is required")
    String name,

    @NotNull(message = "Training date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "Training date cannot be in the past")
    LocalDate date,

    @NotNull(message = "Training duration is required")
    Integer durationInMinutes
) {
}
