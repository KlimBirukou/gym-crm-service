package com.epam.gym.service.user.trainer.dto;

import lombok.NonNull;

public record UpdateTrainerDto(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName
) {
}
