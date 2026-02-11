package com.epam.gym.storage.initializer.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.initializer.AbstractJsonStorageInitializer;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.UUID;

@Component
public class TraineeStorageInitializer extends AbstractJsonStorageInitializer<UUID, Trainee> {

    private static final TypeReference<List<Trainee>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    protected String getEntityName() {
        return Trainee.class.getSimpleName();
    }

    @Override
    protected TypeReference<List<Trainee>> getTypeReference() {
        return TYPE_REFERENCE;
    }
}
