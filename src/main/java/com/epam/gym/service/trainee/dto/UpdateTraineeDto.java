package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

import java.util.UUID;

public record UpdateTraineeDto(

    @NonNull UUID uid,
    @NonNull String address
) {
}
