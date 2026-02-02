package com.epam.gym.repository.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.trainer.InMemoryTrainerStorage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public final class InMemoryTrainerRepository implements ITrainerRepository {

    private final InMemoryTrainerStorage storage;

    @Override
    public void save(@NonNull Trainer trainer) {
        storage.put(trainer.getUid(), trainer);
    }

    @Override
    //TODO: KISS
    // - duplicated with InMemoryTraineeRepository::findByFirstNameAndLastName now, because it will be removed soon.
    public List<Trainer> findByFirstNameAndLastName(@NonNull String firstName,@NonNull String lastName) {
        return storage.values()
            .stream()
            .filter(trainer -> Objects.equals(trainer.getFirstName(), firstName))
            .filter(trainer -> Objects.equals(trainer.getLastName(), lastName))
            .toList();
    }

    @Override
    public Optional<Trainer> findByUid(@NonNull UUID uid) {
        return storage.get(uid);
    }
}
