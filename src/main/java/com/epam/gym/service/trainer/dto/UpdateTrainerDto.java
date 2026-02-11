package com.epam.gym.service.trainer.dto;

import lombok.NonNull;

import java.util.UUID;

public record UpdateTrainerDto(

    @NonNull UUID uid,
    @NonNull String specialization
) {
}
