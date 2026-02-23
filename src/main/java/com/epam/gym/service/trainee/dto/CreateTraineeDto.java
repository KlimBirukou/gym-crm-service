package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

import java.time.LocalDate;

public record CreateTraineeDto(

    @NonNull String firstName,
    @NonNull String lastName,
    LocalDate birthdate,
    String address
) {
}
