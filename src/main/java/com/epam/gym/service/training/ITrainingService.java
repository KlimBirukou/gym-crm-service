package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.training.dto.TrainingsCriteriaDto;
import lombok.NonNull;

import java.util.List;

public interface ITrainingService {

    Training create(@NonNull CreateTrainingDto dto);

    List<Training> getTraineeTrainings(@NonNull TrainingsCriteriaDto username);

    List<Training> getTrainerTrainings(@NonNull TrainingsCriteriaDto username);
}
