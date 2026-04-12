package com.epam.gym.crm.exception.not.found;

import com.epam.gym.crm.domain.user.Trainer;

public class TrainerNotFoundException extends NotFoundException {

    public TrainerNotFoundException(String identifier) {
        super(identifier, Trainer.class.getSimpleName());
    }
}
