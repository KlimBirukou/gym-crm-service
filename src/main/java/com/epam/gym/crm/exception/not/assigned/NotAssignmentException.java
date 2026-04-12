package com.epam.gym.crm.exception.not.assigned;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotAssignmentException extends RuntimeException {

    private final String traineeUsername;
    private final String trainerUsername;
}
