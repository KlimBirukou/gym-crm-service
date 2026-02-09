package com.epam.gym.repository.user.trainee;

import com.epam.gym.domain.user.Trainee;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITraineeRepository {

    boolean existByUid(@NonNull UUID uid);

    void save(@NonNull Trainee trainee);

    List<Trainee> findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName);

    Optional<Trainee> findByUid(@NonNull UUID uid);

    void deleteByUid(@NonNull UUID uid);
}
