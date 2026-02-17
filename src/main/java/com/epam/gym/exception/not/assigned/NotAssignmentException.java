package com.epam.gym.exception.not.assigned;

public class NotAssignmentException extends RuntimeException {

    public static final String MESSAGE = "Trainer [%s] not assigned to [%s] trainee";

    public NotAssignmentException(String trainerIdentifier, String traineeIdentifier) {
        super(MESSAGE.formatted(trainerIdentifier, traineeIdentifier));
    }
}
