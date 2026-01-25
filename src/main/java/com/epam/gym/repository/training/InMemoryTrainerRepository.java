package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public final class InMemoryTrainerRepository implements ITrainingRepository {

    private InMemoryStorage inMemoryStorage;

    @Override
    public void save(Training training) {
        inMemoryStorage.getTrainingStorage().put(training.getTrainingUid(), training);
    }

    @Override
    public List<Training> findByTrainerUid(UUID uid) {
        return inMemoryStorage.getTrainingStorage()
            .values()
            .stream()
            .filter(training -> Objects.equals(training.getTrainerUid(), uid))
            .toList();
    }

    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }
}
