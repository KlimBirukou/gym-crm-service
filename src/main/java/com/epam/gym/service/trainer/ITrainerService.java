package com.epam.gym.service.trainer;

import com.epam.gym.domain.Trainer;

import java.util.List;
import java.util.UUID;

public interface ITrainerService {

    UUID create(CreateTrainerDto dto);

    void update(Trainer trainer);

    List<Trainer> getByFirstLastName(String firstName, String lastName);
}
