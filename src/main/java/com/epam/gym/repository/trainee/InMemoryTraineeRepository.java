package com.epam.gym.repository.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.trainee.InMemoryTraineeStorage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public final class InMemoryTraineeRepository
    implements ITraineeRepository {

    private final InMemoryTraineeStorage storage;

    @Override
    public void save(@NonNull Trainee trainee) {
        storage.put(trainee.getUid(), trainee);
    }

    @Override
    //TODO: KISS
    // - duplicated with InMemoryTrainerRepository::findByFirstNameAndLastName now, because it will be removed soon.
    public List<Trainee> findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName) {
        return storage.values()
            .stream()
            .filter(trainee -> Objects.equals(trainee.getFirstName(), firstName))
            .filter(trainee -> Objects.equals(trainee.getLastName(), lastName))
            .toList();
    }

    @Override
    public Optional<Trainee> findByUid(@NonNull UUID uid) {
        return storage.get(uid);
    }

    @Override
    public void deleteByUid(@NonNull UUID uid) {
        storage.remove(uid);
    }
}
