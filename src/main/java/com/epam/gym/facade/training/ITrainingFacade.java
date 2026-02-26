package com.epam.gym.facade.training;

import com.epam.gym.controlller.rest.training.dto.req.CreateTrainingRequest;
import com.epam.gym.controlller.rest.training.dto.req.GetTraineeTrainingsRequest;
import com.epam.gym.controlller.rest.training.dto.req.GetTrainerTrainingRequest;
import com.epam.gym.controlller.rest.training.dto.res.TraineeTrainingResponse;
import com.epam.gym.controlller.rest.training.dto.res.TrainerTrainingsResponse;
import com.epam.gym.controlller.rest.training.dto.res.TrainingTypeResponse;
import lombok.NonNull;

import java.util.List;

public interface ITrainingFacade {

    void create(@NonNull CreateTrainingRequest request);

    List<TraineeTrainingResponse> getTraineeTrainings(@NonNull GetTraineeTrainingsRequest request);

    List<TrainerTrainingsResponse> getTrainerTrainings(@NonNull GetTrainerTrainingRequest request);

    List<TrainingTypeResponse> getTrainingsTypes();
}
