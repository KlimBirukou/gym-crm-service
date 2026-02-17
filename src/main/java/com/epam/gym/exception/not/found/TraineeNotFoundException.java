package com.epam.gym.exception.not.found;

import com.epam.gym.domain.user.Trainee;

public class TraineeNotFoundException extends NotFoundException {

    public TraineeNotFoundException(String identifier) {
        super(identifier, Trainee.class.getSimpleName());
    }
}
