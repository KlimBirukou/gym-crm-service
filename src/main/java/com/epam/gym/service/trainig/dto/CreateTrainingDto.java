package com.epam.gym.service.trainig.dto;

import com.epam.gym.domain.training.TrainingType;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public record CreateTrainingDto(

    UUID trainerUid,
    UUID traineeUid,
    String trainingName,
    TrainingType trainingType,
    LocalDate trainingDate,
    Duration trainingDuration
) {
}
