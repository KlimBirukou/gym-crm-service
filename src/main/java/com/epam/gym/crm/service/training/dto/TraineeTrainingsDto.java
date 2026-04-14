package com.epam.gym.crm.service.training.dto;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record TraineeTrainingsDto(

    @NonNull String username,
    LocalDate from,
    LocalDate to,
    String trainerUsername,
    String trainingTypeName
) {

}
