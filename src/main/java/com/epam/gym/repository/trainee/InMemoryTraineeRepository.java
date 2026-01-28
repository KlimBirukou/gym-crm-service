package com.epam.gym.repository.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.trainee.InMemoryTraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class InMemoryTraineeRepository implements ITraineeRepository {

    private InMemoryTraineeStorage storage;

    @Override
    public void save(Trainee trainee) {
        storage.put(trainee.getUid(), trainee);
    }

    @Override
    public List<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        return storage.values()
            .stream()
            .filter(trainee -> Objects.equals(trainee.getFirstName(), firstName))
            .filter(trainee -> Objects.equals(trainee.getLastName(), lastName))
            .toList();
    }

    @Override
    public Optional<Trainee> findByUid(UUID uid) {
        return storage.get(uid);
    }

    @Override
    public void deleteByUid(UUID uid) {
        storage.remove(uid);
    }

    @Autowired
    public void InMemoryTraineeStorage(InMemoryTraineeStorage storage) {
        this.storage = storage;
    }
}
