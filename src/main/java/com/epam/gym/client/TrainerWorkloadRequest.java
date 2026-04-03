package com.epam.gym.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TrainerWorkloadRequest(

    String trainerUsername,
    LocalDate trainingDate,
    int trainingDuration,
    ActionType actionType
) {

}
