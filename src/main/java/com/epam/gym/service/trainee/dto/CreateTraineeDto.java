package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

import java.time.LocalDate;

public record CreateTraineeDto (

    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String address,
    @NonNull LocalDate birthdate
) {
}
