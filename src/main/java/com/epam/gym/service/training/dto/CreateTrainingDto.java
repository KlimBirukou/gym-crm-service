package com.epam.gym.service.training.dto;

import com.epam.gym.domain.training.TrainingType;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public record CreateTrainingDto(

    @NonNull UUID traineeUid,
    @NonNull UUID trainerUid,
    @NonNull String name,
    @NonNull TrainingType type,
    @NonNull LocalDate date,
    @NonNull Duration duration
) {
}
