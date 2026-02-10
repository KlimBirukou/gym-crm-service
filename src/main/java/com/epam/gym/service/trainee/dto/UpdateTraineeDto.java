package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

import java.util.UUID;

public record UpdateTraineeDto(

    @NonNull UUID uid,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String username,
    @NonNull String address
) {
}
