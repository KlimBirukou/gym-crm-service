package com.epam.gym.exception.not.found;

import com.epam.gym.domain.user.User;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String identifier) {
        super(identifier, User.class.getSimpleName());
    }
}
