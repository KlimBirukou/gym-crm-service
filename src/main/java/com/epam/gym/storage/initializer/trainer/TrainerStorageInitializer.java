package com.epam.gym.storage.initializer.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.initializer.AbstractJsonStorageInitializer;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.UUID;

@Component
public class TrainerStorageInitializer extends AbstractJsonStorageInitializer<UUID, Trainer> {

    private static final TypeReference<List<Trainer>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    protected String getEntityName() {
        return Trainer.class.getSimpleName();
    }

    @Override
    protected TypeReference<List<Trainer>> getTypeReference() {
        return TYPE_REFERENCE;
    }
}
