package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.training.InMemoryTrainingStorage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InMemoryTrainingRepository implements ITrainingRepository {

    //@Value("${storage}")
    private final InMemoryTrainingStorage storage;

    @Override
    public void save(@NonNull Training training) {
        storage.put(training.getUid(), training);
    }

    @Override
    public List<Training> findByLocalDate(@NonNull LocalDate date) {
        return storage.values()
            .stream()
            .filter(training -> training.getDate().isEqual(date))
            .toList();
    }

    @Override
    public List<Training> findByTraineeUid(@NonNull UUID uid) {
        return storage.values()
            .stream()
            .filter(training -> Objects.equals(training.getTraineeUid(), uid))
            .toList();
    }
}
