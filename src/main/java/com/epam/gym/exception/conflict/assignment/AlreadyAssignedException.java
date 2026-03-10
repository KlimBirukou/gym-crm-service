package com.epam.gym.exception.conflict.assignment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AlreadyAssignedException extends RuntimeException {

    private final String traineeUsername;
    private final String trainerUsername;
}
