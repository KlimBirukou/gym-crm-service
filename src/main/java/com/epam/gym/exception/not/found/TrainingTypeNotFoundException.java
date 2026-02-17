package com.epam.gym.exception.not.found;

import com.epam.gym.domain.training.TrainingType;

public class TrainingTypeNotFoundException extends NotFoundException {

    public TrainingTypeNotFoundException(String identifier) {
        super(identifier, TrainingType.class.getSimpleName());
    }
}
