package com.epam.gym.exception;

import lombok.Getter;

@Getter
public class DateValidationException extends ValidationException {

    public static final String DOMAIN_NOT_FOUND = "Creating %s not possible because bad date";
    private final String domainType;

    public DateValidationException(String domainType) {
        super(DOMAIN_NOT_FOUND.formatted(domainType));
        this.domainType = domainType;
    }
}
