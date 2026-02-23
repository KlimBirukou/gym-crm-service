package com.epam.gym.exception.not.found;

public abstract class NotFoundException extends RuntimeException {

    public static final String MESSAGE = "%s [%s] was not found";

    protected NotFoundException(String identifier, String entityName) {
        super(MESSAGE.formatted(entityName, identifier));
    }
}
