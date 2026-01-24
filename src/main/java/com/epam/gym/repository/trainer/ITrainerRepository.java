package com.epam.gym.repository.trainer;

import com.epam.gym.domain.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITrainerRepository {

    void save(Trainer trainer);

    Optional<Trainer> findByUid(UUID uid);

    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName);

    void deleteByUid(UUID uid);
}

