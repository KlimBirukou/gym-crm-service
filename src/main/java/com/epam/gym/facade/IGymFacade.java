package com.epam.gym.facade;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import lombok.NonNull;

import java.util.UUID;

public interface IGymFacade {

    Trainer createTrainer(@NonNull CreateTrainerDto dto);

    void updateTrainer(@NonNull UpdateTrainerDto dto);

    Trainee createTrainee(@NonNull CreateTraineeDto dto);

    void updateTrainee(@NonNull UpdateTraineeDto dto);

    void deleteTrainee(@NonNull UUID uid);

    Training createTraining(@NonNull CreateTrainingDto dto);
}
