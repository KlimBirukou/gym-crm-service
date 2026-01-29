package com.epam.gym.storage.initializer.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.initializer.AbstractJsonStorageInitializer;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.UUID;

@Component
public class TrainingStorageInitializer extends AbstractJsonStorageInitializer<UUID, Training> {

    public static final TypeReference<List<Training>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    protected String getEntityName() {
        return Training.class.getSimpleName();
    }

    @Override
    protected TypeReference<List<Training>> getTypeReference() {
        return TYPE_REFERENCE;
    }
}
