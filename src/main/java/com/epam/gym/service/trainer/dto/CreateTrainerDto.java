package com.epam.gym.service.trainer.dto;

import lombok.NonNull;

import java.time.LocalDate;

public record CreateTrainerDto(

    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String specialization
) {
}
