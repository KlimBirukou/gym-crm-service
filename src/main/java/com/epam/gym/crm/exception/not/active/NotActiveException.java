package com.epam.gym.crm.exception.not.active;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class NotActiveException extends RuntimeException {

    private final String identifier;
    private final String entityName;
}
