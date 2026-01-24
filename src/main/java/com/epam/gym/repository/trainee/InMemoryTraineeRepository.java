package com.epam.gym.repository.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class InMemoryTraineeRepository implements ITraineeRepository {

    private InMemoryStorage inMemoryStorage;

    @Override
    public void save(Trainee trainee) {
        inMemoryStorage.getTraineeStorage().put(trainee.getUid(), trainee);
    }

    @Override
    public List<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        return inMemoryStorage.getTraineeStorage()
            .values()
            .stream()
            .filter(trainee -> Objects.equals(trainee.getFirstName(), firstName))
            .filter(trainee -> Objects.equals(trainee.getLastName(), lastName))
            .toList();
    }

    @Override
    public Optional<Trainee> findByUid(UUID uid) {
        return Optional.ofNullable(inMemoryStorage.getTraineeStorage().get(uid));
    }

    @Override
    public void deleteByUid(UUID uid) {
        inMemoryStorage.getTraineeStorage().remove(uid);
    }

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }
}
