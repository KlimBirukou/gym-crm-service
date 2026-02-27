package com.epam.gym.controller.rest.training.dto.response;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record TrainerTrainingsResponse(

    @NonNull String name,
    @NonNull LocalDate date,
    @NonNull String trainingTypeName,
    int duration,
    @NonNull String traineeUsername
) {
}
