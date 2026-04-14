package com.epam.gym.crm.service.training;

import com.epam.gym.crm.domain.training.Training;
import com.epam.gym.crm.service.training.dto.CreateTrainingDto;
import com.epam.gym.crm.service.training.dto.TraineeTrainingsDto;
import com.epam.gym.crm.service.training.dto.TrainerTrainingsDto;
import lombok.NonNull;

import java.util.List;

public interface ITrainingService {

    Training create(@NonNull CreateTrainingDto dto);

    List<Training> getTraineeTrainings(@NonNull TraineeTrainingsDto username);

    List<Training> getTrainerTrainings(@NonNull TrainerTrainingsDto username);
}
