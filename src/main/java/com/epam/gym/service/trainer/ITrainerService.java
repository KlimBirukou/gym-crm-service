package com.epam.gym.service.trainer;

import com.epam.gym.domain.Trainer;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;

public interface ITrainerService {

    Trainer create(CreateTrainerDto dto);

    void update(UpdateTrainerDto dto);
}
