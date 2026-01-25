package com.epam.gym.service.trainig;

import com.epam.gym.domain.training.Training;
import com.epam.gym.service.trainig.dto.CreateTrainingDto;

public interface ITrainingService {

    Training create(CreateTrainingDto dto);
}
