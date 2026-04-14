package com.epam.gym.crm.service.training.dto;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record CreateTrainingDto(

    @NonNull String traineeUsername,
    @NonNull String trainerUsername,
    @NonNull String name,
    @NonNull LocalDate date,
    int durationInMinutes
) {

}
