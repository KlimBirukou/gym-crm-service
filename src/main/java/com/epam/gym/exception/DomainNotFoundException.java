package com.epam.gym.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DomainNotFoundException extends RuntimeException {

    public static final String DOMAIN_NOT_FOUND = "%s for parameter %s not found";
    private final String domainId;
    private final String domainType;

    public DomainNotFoundException(String entityType, String param) {
        super(DOMAIN_NOT_FOUND.formatted(entityType, param));
        this.domainType = entityType;
        this.domainId = param;
    }
}
