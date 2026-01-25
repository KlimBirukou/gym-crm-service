package com.epam.gym.facade;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.trainig.dto.CreateTrainingDto;

import java.util.UUID;

public interface IGymFacade {

    Trainer createTrainer(CreateTrainerDto dto);

    void updateTrainer(UpdateTrainerDto dto);

    Trainee createTrainee(CreateTraineeDto dto);

    void updateTrainee(UpdateTraineeDto dto);

    void deleteTrainee(UUID uid);

    Training createTraining(CreateTrainingDto dto);
}
