package com.epam.gym.service.training.dto;

import lombok.NonNull;

import java.time.LocalDate;

public record TrainingsCriteriaDto(

    @NonNull String username,
    @NonNull String trainingType,
    @NonNull LocalDate from,
    @NonNull LocalDate to
) {
}
