package com.epam.gym.service.generator.name.factory;

import com.epam.gym.GymApplication;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UsernameFactory implements IUsernameFactory {

    private static final String MESSAGE = "Suffix must be positive";

    @Override
    public String create(@NonNull String firstName, @NonNull String lastName) {
        return String.join(GymApplication.DEFAULT_USERNAME_DELIMITER, firstName, lastName);
    }

    @Override
    public String create(@NonNull String firstName, @NonNull String lastName, int suffix) {
        if (suffix < 0) {
            throw new IllegalArgumentException(MESSAGE);
        }
        return String.join(GymApplication.DEFAULT_USERNAME_DELIMITER, firstName, lastName, String.valueOf(suffix + 1));
    }
}
