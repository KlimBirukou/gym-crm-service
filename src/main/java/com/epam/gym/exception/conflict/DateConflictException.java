package com.epam.gym.exception.conflict;

import java.time.LocalDate;

public class DateConflictException extends RuntimeException {

    public static final String MESSAGE = "%s [%s] already has a training on [%s] date";

    public DateConflictException(String identifier, String username, LocalDate date) {
        super(MESSAGE.formatted(identifier, username, date));
    }
}
