package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.training.InMemoryTrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public final class InMemoryTrainingRepository implements ITrainingRepository {

    private InMemoryTrainingStorage storage;

    @Override
    public void save(Training training) {
        storage.put(training.getTrainingUid(), training);
    }

    @Override
    public List<Training> findByLocalDate(LocalDate date) {
        return storage
            .values()
            .stream()
            .filter(training -> training.getTrainingDate().isEqual(date))
            .toList();
    }

    @Autowired
    public void setInMemoryTrainingStorage(InMemoryTrainingStorage storage) {
        this.storage = storage;
    }
}
