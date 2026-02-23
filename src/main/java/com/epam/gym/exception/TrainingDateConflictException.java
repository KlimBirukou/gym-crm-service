package com.epam.gym.exception;

import java.time.LocalDate;

public class TrainingDateConflictException extends RuntimeException {

    public static final String MESSAGE = "Trainee [%s] or trainer [%s] already has a training on [%s]";

    public TrainingDateConflictException(String traineeIdentifier, String trainerIdentifier, LocalDate date) {
        super(MESSAGE.formatted(traineeIdentifier, trainerIdentifier, date));
    }
}
