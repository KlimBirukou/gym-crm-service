package com.epam.gym.service.generator.name.factory;

import com.epam.gym.configuration.properties.UserProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameFactory implements IUsernameFactory {

    private static final String MESSAGE = "Suffix must be positive";

    private final UserProperties userProperties;

    @Override
    public String create(@NonNull String firstName, @NonNull String lastName) {
        return String.join(userProperties.usernameDelimiter(), firstName, lastName);
    }

    @Override
    public String create(@NonNull String firstName, @NonNull String lastName, int suffix) {
        if (suffix < 0) {
            throw new IllegalArgumentException(MESSAGE);
        }
        return String.join(userProperties.usernameDelimiter(), firstName, lastName, String.valueOf(suffix + 1));
    }
}
