package com.epam.gym.exception.not.found;

import com.epam.gym.domain.user.Trainer;

public class TrainerNotFoundException extends NotFoundException {

    public TrainerNotFoundException(String identifier) {
        super(identifier, Trainer.class.getSimpleName());
    }
}
