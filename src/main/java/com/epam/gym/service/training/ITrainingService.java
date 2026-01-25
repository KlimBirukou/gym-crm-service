package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.service.training.dto.CreateTrainingDto;

public interface ITrainingService {

    Training create(CreateTrainingDto dto);
}
