package com.epam.gym.controller.rest.trainer.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record TraineeProfileResponse(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName
) {
}
