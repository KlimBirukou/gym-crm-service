package com.epam.gym.repository.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.trainer.InMemoryTrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class InMemoryTrainerRepository implements ITrainerRepository {

    private InMemoryTrainerStorage storage;

    @Override
    public void save(Trainer trainer) {
        storage.put(trainer.getUid(), trainer);
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        return storage.values()
            .stream()
            .filter(trainer -> Objects.equals(trainer.getFirstName(), firstName))
            .filter(trainer -> Objects.equals(trainer.getLastName(), lastName))
            .toList();
    }

    @Override
    public Optional<Trainer> findByUid(UUID uid) {
        return storage.get(uid);
    }

    @Autowired
    public void setInMemoryTrainerStorage(InMemoryTrainerStorage trainerStorage) {
        this.storage = trainerStorage;
    }
}
