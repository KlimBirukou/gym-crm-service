package com.epam.gym.exception.conflict.assignment;

public class AlreadyAssignedException extends RuntimeException {

    public static final String MESSAGE = "Trainee [%s] already assigned to trainer [%s]";

    public AlreadyAssignedException(String traineeIdentifier, String trainerIdentifier) {
        super(MESSAGE.formatted(traineeIdentifier, trainerIdentifier));
    }
}
