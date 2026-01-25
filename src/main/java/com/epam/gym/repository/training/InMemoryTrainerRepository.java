package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public final class InMemoryTrainerRepository implements ITrainingRepository {

    private InMemoryStorage inMemoryStorage;

    @Override
    public void save(Training training) {
        inMemoryStorage.getTrainingStorage().put(training.getTrainingUid(), training);
    }

    @Override
    public List<Training> findByLocalDate(LocalDate date) {
        return inMemoryStorage.getTrainingStorage()
            .values()
            .stream()
            .filter(training -> training.getTrainingDate().isEqual(date))
            .toList();
    }

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }
}
