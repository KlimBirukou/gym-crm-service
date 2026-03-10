package com.epam.gym.exception.conflict.date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class DateConflictException extends RuntimeException {

    private final String identifier;
    private final String entityName;
    private final LocalDate date;
}
