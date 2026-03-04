package com.epam.gym.service.trainee.dto;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record UpdateTraineeDto(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull LocalDate birthdate,
    @NonNull String address
) {

}
