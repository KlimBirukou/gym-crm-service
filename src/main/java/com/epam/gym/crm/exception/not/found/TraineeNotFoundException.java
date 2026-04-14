package com.epam.gym.crm.exception.not.found;

import com.epam.gym.crm.domain.user.Trainee;

public class TraineeNotFoundException extends NotFoundException {

    public TraineeNotFoundException(String identifier) {
        super(identifier, Trainee.class.getSimpleName());
    }
}
