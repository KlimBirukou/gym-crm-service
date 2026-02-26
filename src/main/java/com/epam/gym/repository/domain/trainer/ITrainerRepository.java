package com.epam.gym.repository.domain.trainer;

import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITrainerRepository {

    boolean existsByUsername(@NonNull String username);

    Optional<Trainer> getByUsername(@NonNull String username);

    void save(@NonNull Trainer trainer);

    List<Trainer> getByFirstNameAndLastName(@NonNull String firstname, @NonNull String lastName);

    List<Trainer> findAllByUids(@NonNull List<UUID> uids);
}
