package com.epam.gym.controller.rest.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.facade.training.ITrainingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainingController implements ITrainingController {

    private final ITrainingFacade trainingFacade;

    @Override
    public void createTraining(CreateTrainingRequest request) {
        trainingFacade.create(request);
    }

    @Override
    public List<TraineeTrainingResponse> getTraineeTrainings(String username,
                                                             LocalDate from,
                                                             LocalDate to,
                                                             String trainerUsername,
                                                             String trainingTypeName) {
        var request = GetTraineeTrainingsRequest.builder()
            .username(username)
            .from(from)
            .to(to)
            .trainerUsername(trainerUsername)
            .trainingTypeName(trainingTypeName)
            .build();
        return trainingFacade.getTraineeTrainings(request);
    }

    @Override
    public List<TrainerTrainingsResponse> getTrainerTrainings(String username,
                                                              LocalDate from,
                                                              LocalDate to,
                                                              String traineeUsername) {
        var request = GetTrainerTrainingRequest.builder()
            .username(username)
            .from(from)
            .to(to)
            .traineeUsername(traineeUsername)
            .build();
        return trainingFacade.getTrainerTrainings(request);
    }

    @Override
    public List<TrainingTypeResponse> getTrainingTypes() {
        return trainingFacade.getTrainingsTypes();
    }
}
