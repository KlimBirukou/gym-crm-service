package com.epam.gym.controller.rest.trainee.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record TrainerProfileResponse(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String specializationName
) {
}
