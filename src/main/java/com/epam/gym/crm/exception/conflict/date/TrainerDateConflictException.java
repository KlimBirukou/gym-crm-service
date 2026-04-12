package com.epam.gym.crm.exception.conflict.date;

import com.epam.gym.crm.domain.user.Trainer;

import java.time.LocalDate;

public class TrainerDateConflictException extends DateConflictException {

    public TrainerDateConflictException(String username, LocalDate date) {
        super(Trainer.class.getSimpleName(), username, date);
    }
}
