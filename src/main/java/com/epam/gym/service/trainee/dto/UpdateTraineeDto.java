package com.epam.gym.service.user.trainee.dto;

import lombok.NonNull;

import java.time.LocalDate;

public record UpdateTraineeDto(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull LocalDate birthdate,
    @NonNull String address
) {
}
