package com.epam.gym.service.name;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public final class DefaultUserNameGenerator implements IUsernameGenerator {

    private static final String DELIMITER = "";

    @Override
    public String generate(String firstName, String lastName) {
        return String.join(normalize(firstName), normalize(lastName));
    }

    private String normalize(String value) {
        return Objects.isNull(value)
            ? ""
            : value.trim();
    }
}
