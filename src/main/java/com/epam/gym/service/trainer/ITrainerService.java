package com.epam.gym.service.trainer;

import com.epam.gym.domain.Trainer;

public interface ITrainerService {

    Trainer create(CreateTrainerDto dto);

    void update(Trainer trainer);
}
