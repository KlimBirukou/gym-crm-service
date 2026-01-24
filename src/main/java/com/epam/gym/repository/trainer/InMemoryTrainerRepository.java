package com.epam.gym.repository.trainer;

import com.epam.gym.domain.Trainer;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class InMemoryTrainerRepository implements ITrainerRepository {

    private InMemoryStorage inMemoryStorage;

    @Override
    public void save(Trainer trainer) {
        inMemoryStorage.getTrainerStorage().put(trainer.getUid(), trainer);
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        return inMemoryStorage.getTrainerStorage()
            .values()
            .stream()
            .filter(trainer -> Objects.equals(trainer.getFirstName(), firstName))
            .filter(trainer -> Objects.equals(trainer.getLastName(), lastName))
            .toList();
    }

    @Override
    public Optional<Trainer> findByUid(UUID uid) {
        return Optional.ofNullable(inMemoryStorage.getTrainerStorage().get(uid));
    }

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }
}
