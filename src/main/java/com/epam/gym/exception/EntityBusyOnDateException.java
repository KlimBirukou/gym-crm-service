package com.epam.gym.exception;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class EntityBusyOnDateException extends ValidationException {

    public static final String ENTITY_ALREADY_HAS_TRAINING_ON_DATE = "%s with uid = %s already has a training on date = %s";
    private final UUID entityId;
    private final String entityType;
    private final LocalDate date;

    public EntityBusyOnDateException(String entityType, UUID entityId, LocalDate date) {
        super(ENTITY_ALREADY_HAS_TRAINING_ON_DATE.formatted(entityType, entityId, date));
        this.entityType = entityType;
        this.entityId = entityId;
        this.date = date;
    }
}
