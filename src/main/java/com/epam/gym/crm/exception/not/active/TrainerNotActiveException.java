package com.epam.gym.crm.exception.not.active;

import com.epam.gym.crm.domain.user.Trainer;

public class TrainerNotActiveException extends NotActiveException {

    public TrainerNotActiveException(String identifier) {
        super(identifier, Trainer.class.getSimpleName());
    }
}
