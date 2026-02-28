package com.epam.gym.controller.rest.trainer.dto.response;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record TrainerResponse(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String specialization,
    @NonNull Boolean active,
    @NonNull List<TraineeProfileResponse> trainees
) {
}
