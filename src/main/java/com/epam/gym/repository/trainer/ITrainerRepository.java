package com.epam.gym.repository.trainer;

import com.epam.gym.domain.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITrainerRepository {

    void save(Trainer trainer);

    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Trainer> findByUid(UUID uid);
}

