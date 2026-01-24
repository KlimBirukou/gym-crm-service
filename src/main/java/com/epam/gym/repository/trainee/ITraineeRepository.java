package com.epam.gym.repository.trainee;

import com.epam.gym.domain.user.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITraineeRepository {

    void save(Trainee trainee);

    List<Trainee> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Trainee> findByUid(UUID uid);

    void deleteByUid(UUID uid);
}
