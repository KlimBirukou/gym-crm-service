package com.epam.gym.exception.conflict;

import com.epam.gym.domain.user.Trainer;

import java.time.LocalDate;

public class TrainerDateConflictException extends DateConflictException {

    public TrainerDateConflictException(String username, LocalDate date) {
        super(Trainer.class.getSimpleName(), username, date);
    }
}
