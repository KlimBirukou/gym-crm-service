package com.epam.gym.service.training.dto;

import lombok.NonNull;

import java.time.LocalDate;

public record CreateTrainingDto(

    @NonNull String traineeUsername,
    @NonNull String trainerUsername,
    @NonNull String name,
    @NonNull String type,
    @NonNull LocalDate date,
    int durationInMinutes
) {
}
