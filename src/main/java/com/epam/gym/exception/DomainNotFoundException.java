package com.epam.gym.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DomainNotFoundException extends RuntimeException {

    public static final String DOMAIN_NOT_FOUND = "%s with uid = %s not found";
    private final UUID domainId;
    private final String domainType;

    public DomainNotFoundException(String entityType, UUID entityId) {
        super(DOMAIN_NOT_FOUND.formatted(entityType, entityId));
        this.domainType = entityType;
        this.domainId = entityId;
    }
}
