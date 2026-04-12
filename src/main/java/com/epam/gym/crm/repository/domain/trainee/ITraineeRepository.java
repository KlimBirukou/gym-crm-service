package com.epam.gym.crm.repository.domain.trainee;

import com.epam.gym.crm.domain.user.Trainee;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITraineeRepository {

    boolean existsByUsername(@NonNull String username);

    Optional<Trainee> getByUsername(@NonNull String username);

    void save(@NonNull Trainee trainee);

    void update(@NonNull Trainee trainee);

    void deleteByUsername(@NonNull String username);

    List<Trainee> getByFirstNameAndLastName(@NonNull String firstname, @NonNull String lastName);

    List<Trainee> findAllByUids(@NonNull List<UUID> uids);
}
