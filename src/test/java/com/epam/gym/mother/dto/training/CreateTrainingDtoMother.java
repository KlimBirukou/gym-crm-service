package com.epam.gym.mother.dto.training;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.service.training.dto.CreateTrainingDto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public class CreateTrainingDtoMother {

    public static CreateTrainingDto get(UUID traineeUid, UUID trainerUid, String name, LocalDate date) {
        return new CreateTrainingDto(
            traineeUid,
            trainerUid,
            name,
            TrainingType.CARDIO,
            date,
            Duration.ZERO
        );
    }
}
