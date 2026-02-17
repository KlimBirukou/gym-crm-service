package com.epam.gym.exception;

public class TrainingTypeMismatchException extends RuntimeException {

    public static final String MESSAGE = "Trainer [%s] cannot conduct with [%s]-type training";

    public TrainingTypeMismatchException(String trainerIdentifier, String trainingTypeIdentifier) {
        super(MESSAGE.formatted(trainerIdentifier, trainingTypeIdentifier));
    }
}
