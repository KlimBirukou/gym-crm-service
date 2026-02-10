package com.epam.gym.validator.base;

import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.validator.IValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public abstract class AbstractDomainPersistenceValidator implements IValidator<UUID> {

    private final Predicate<UUID> existenceChecker;
    private final String entityType;

    @Override
    public void validate(@NonNull UUID uid) {
        if (!existenceChecker.test(uid)) {
            throw new DomainNotFoundException(entityType, uid.toString());
        }
    }
}
