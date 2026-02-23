package com.epam.gym.exception.not.active;

import com.epam.gym.domain.user.Trainee;

public class TraineeNotActiveException extends NotActiveException {

    public TraineeNotActiveException(String identifier) {
        super(identifier, Trainee.class.getSimpleName());
    }
}
