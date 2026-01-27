package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

public record CreateTraineeDto (

    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String address
) {
}
