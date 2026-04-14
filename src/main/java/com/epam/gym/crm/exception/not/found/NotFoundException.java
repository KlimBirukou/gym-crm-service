package com.epam.gym.crm.exception.not.found;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class NotFoundException extends RuntimeException {

    private final String identifier;
    private final String entityName;
}
