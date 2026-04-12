package com.epam.gym.crm.facade.training;

import com.epam.gym.crm.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.crm.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.crm.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.crm.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.crm.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.crm.controller.rest.training.dto.response.TrainingTypeResponse;
import lombok.NonNull;

import java.util.List;

public interface ITrainingFacade {

    void create(@NonNull CreateTrainingRequest request);

    List<TraineeTrainingResponse> getTraineeTrainings(@NonNull GetTraineeTrainingsRequest request);

    List<TrainerTrainingsResponse> getTrainerTrainings(@NonNull GetTrainerTrainingRequest request);

    List<TrainingTypeResponse> getTrainingsTypes();
}
