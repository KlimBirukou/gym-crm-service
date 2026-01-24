package com.epam.gym.repository.trainer;

import com.epam.gym.domain.Trainer;
import com.epam.gym.storage.TrainerStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public final class InMemoryTrainerRepository implements ITrainerRepository {

    private final TrainerStorage trainerStorage;

    @Override
    public void save(Trainer trainer) {
        trainerStorage.getStorage().put(trainer.getUid(), trainer);
    }

    @Override
    public Optional<Trainer> findByUid(UUID uid) {
        return Optional.ofNullable(trainerStorage.getStorage().get(uid));
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        return trainerStorage.getStorage()
            .values()
            .stream()
            .filter(trainer -> Objects.equals(trainer.getFirstName(), firstName)
                && Objects.equals(trainer.getLastName(), lastName))
            .toList();
    }

    @Override
    public void deleteByUid(UUID uid) {
        trainerStorage.getStorage().remove(uid);
    }
}
