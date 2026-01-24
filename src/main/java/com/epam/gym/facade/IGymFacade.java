package com.epam.gym.facade;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;

public interface IGymFacade {

    Trainer createTrainer(CreateTrainerDto dto);

    void updateTrainer(UpdateTrainerDto dto);
}
