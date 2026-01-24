package com.epam.gym.service.generator.name.factory;

import com.epam.gym.GymApplication;
import org.springframework.stereotype.Service;

@Service
public class UsernameFactory implements IUsernameFactory {

    @Override
    public String create(String firstName, String lastName) {
        return String.join(GymApplication.DEFAULT_USERNAME_DELIMITER, firstName, lastName);
    }

    @Override
    public String create(String firstName, String lastName, int suffix) {
        return String.join(GymApplication.DEFAULT_USERNAME_DELIMITER, firstName, lastName, String.valueOf(suffix + 1));
    }
}
