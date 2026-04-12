package com.epam.gym.crm.exception.not.active;

import com.epam.gym.crm.domain.user.Trainee;

public class TraineeNotActiveException extends NotActiveException {

    public TraineeNotActiveException(String identifier) {
        super(identifier, Trainee.class.getSimpleName());
    }
}
