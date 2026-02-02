package com.epam.gym.storage.initializer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractJsonStorageInitializer<K, V> implements IStorageInitializer<V> {

    public static final String DATA_FILE_FOR_S_NOT_FOUND = "Data file for %s not found";
    public static final String STORAGE_INITIALIZATION_FAILED_FOR = "Storage initialization failed for %s";

    private ObjectMapper objectMapper;

    @Override
    public final List<V> load(@NonNull Resource dataFile) {
        log.info("{} storage initialization started", getEntityName());
        if (!dataFile.exists()) {
            throw new RuntimeException(DATA_FILE_FOR_S_NOT_FOUND.formatted(getEntityName()));
        }
        var entities = List.<V>of();
        try (var inputStream = dataFile.getInputStream()) {
            entities = objectMapper.readValue(inputStream, getTypeReference());
        } catch (IOException exception) {
            throw new RuntimeException(STORAGE_INITIALIZATION_FAILED_FOR.formatted(getEntityName()));
        }
        log.info("{} storage initialized successfully with {} records", getEntityName(), entities.size());
        return entities;
    }

    protected abstract String getEntityName();

    protected abstract TypeReference<List<V>> getTypeReference();

    @Autowired
    public void setObjectMapper(@Qualifier("fromFileParsingObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
