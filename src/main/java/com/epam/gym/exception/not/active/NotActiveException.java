package com.epam.gym.exception.not.active;

public abstract class NotActiveException extends RuntimeException {

    public static final String MESSAGE = "%s [%s] not active to perform this action";

    public NotActiveException(String identifier, String entityName) {
        super(MESSAGE.formatted(entityName, identifier));
    }
}
