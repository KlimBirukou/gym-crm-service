package com.epam.gym.exception;

public class AuthException extends RuntimeException {

    public static final String MESSAGE = "Username or password invalid";

    public AuthException() {
        super(MESSAGE);
    }
}
