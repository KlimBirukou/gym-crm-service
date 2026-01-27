package com.epam.gym.service.training.dto;

import com.epam.gym.domain.training.TrainingType;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public record CreateTrainingDto(

    @NonNull UUID trainerUid,
    @NonNull UUID traineeUid,
    @NonNull String trainingName,
    @NonNull TrainingType trainingType,
    @NonNull LocalDate trainingDate,
    @NonNull Duration trainingDuration
) {
}
