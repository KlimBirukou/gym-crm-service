package com.epam.gym.service.generator.password;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class DefaultPasswordGenerator
    implements IPasswordGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 10);
    }
}
