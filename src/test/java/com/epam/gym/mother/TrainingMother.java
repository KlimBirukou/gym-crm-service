package com.epam.gym.mother;

import com.epam.gym.domain.training.Training;

import java.time.LocalDate;
import java.util.UUID;

public class TrainingMother {

    public static Training get(UUID uid, LocalDate date) {
        return Training.builder()
            .uid(uid)
            .date(date)
            .build();
    }

    public static Training getWithTraineeUid(UUID uid) {
        return Training.builder()
            .traineeUid(uid)
            .build();
    }

    public static Training getWithTrainerUid(UUID uid) {
        return Training.builder()
            .trainerUid(uid)
            .build();
    }
}
