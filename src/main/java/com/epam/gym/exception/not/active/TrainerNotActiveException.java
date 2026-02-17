package com.epam.gym.exception.not.active;

import com.epam.gym.domain.user.Trainer;

public class TrainerNotActiveException extends NotActiveException {

    public TrainerNotActiveException(String identifier) {
        super(identifier, Trainer.class.getSimpleName());
    }
}
