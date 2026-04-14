package com.epam.gym.crm.exception.not.found;

import com.epam.gym.crm.domain.training.TrainingType;

public class TrainingTypeNotFoundException extends NotFoundException {

    public TrainingTypeNotFoundException(String identifier) {
        super(identifier, TrainingType.class.getSimpleName());
    }
}
