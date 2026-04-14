package com.epam.gym.crm.exception.not.found;

import com.epam.gym.crm.domain.user.User;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String identifier) {
        super(identifier, User.class.getSimpleName());
    }
}
