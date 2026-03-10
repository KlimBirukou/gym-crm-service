package com.epam.gym.exception.conflict.date;

import com.epam.gym.domain.user.Trainee;

import java.time.LocalDate;

public class TraineeDateConflictException extends DateConflictException {

    public TraineeDateConflictException(String username, LocalDate date) {
        super(Trainee.class.getSimpleName(), username, date);
    }
}
