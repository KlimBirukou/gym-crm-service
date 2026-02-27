package com.epam.gym.controller.rest.training.dto.response;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record TrainingTypeResponse(

    @NonNull UUID uid,
    @NonNull String name
) {
}
