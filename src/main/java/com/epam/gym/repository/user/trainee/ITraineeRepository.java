package com.epam.gym.repository.user.trainee;

import com.epam.gym.domain.user.Trainee;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITraineeRepository {

    boolean existByUid(@NonNull UUID uid);

    void save(@NonNull Trainee trainee);

    Optional<Trainee> findByUid(@NonNull UUID uid);

    Optional<Trainee> findByUsername(@NonNull String uid);

    List<Trainee> findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName);

    void deleteByUid(@NonNull UUID uid);
}
