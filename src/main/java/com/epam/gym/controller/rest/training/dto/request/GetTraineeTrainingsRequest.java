package com.epam.gym.controller.rest.training.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetTraineeTrainingsRequest(

    @NotBlank(message = "Trainee username is required")
    String username,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate from,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate to,
    String trainerUsername,
    String trainingTypeName
) {

}
