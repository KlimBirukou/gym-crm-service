package com.epam.gym.controller.rest.trainee.dto.response;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TraineeResponse(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull LocalDate birthdate,
    @NonNull String address,
    @NonNull Boolean active,
    @NonNull List<TrainerProfileResponse> trainers
) {
}
