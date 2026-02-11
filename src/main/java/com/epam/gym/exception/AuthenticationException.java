package com.epam.gym.exception;

public class AuthenticationException extends ValidationException {

    private static final String MESSAGE = "Authentication failed for user: %s";

    public AuthenticationException(String username) {
        super(MESSAGE.formatted(username));
    }
}
