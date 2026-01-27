package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import lombok.NonNull;

public interface ITrainingService {

    Training create(@NonNull CreateTrainingDto dto);
}
