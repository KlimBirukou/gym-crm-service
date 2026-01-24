package com.epam.gym.service.password;

import java.util.UUID;

public final class DefaultPasswordGenerator implements IPasswordGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 10);
    }
}
