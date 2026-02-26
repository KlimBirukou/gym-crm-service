package com.epam.gym.controlller.rest.training.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record GetTrainerTrainingRequest(

    @NotBlank(message = "Trainer username is required")
    String username,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate from,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate to,

    String traineeUsername
) {
}
