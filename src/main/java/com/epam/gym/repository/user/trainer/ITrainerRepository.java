package com.epam.gym.repository.user.trainer;

import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITrainerRepository {

    boolean existByUid(@NonNull UUID uid);

    void save(@NonNull Trainer trainer);

    List<Trainer> findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName);

    Optional<Trainer> findByUid(@NonNull UUID uid);
}
