package com.epam.gym.repository.trainer;

import com.epam.gym.domain.Trainer;

import java.util.List;

public interface ITrainerRepository {

    void save(Trainer trainer);

    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName);
}

