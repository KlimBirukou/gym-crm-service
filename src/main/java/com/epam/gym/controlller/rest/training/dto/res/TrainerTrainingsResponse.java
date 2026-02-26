package com.epam.gym.controlller.rest.training.dto.res;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerTrainingsResponse(

    String name,

    LocalDate date,

    String trainingTypeName,

    int duration,

    String traineeUsername
) {
}
