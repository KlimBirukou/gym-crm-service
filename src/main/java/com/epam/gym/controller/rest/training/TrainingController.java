package com.epam.gym.controller.rest.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.facade.training.ITrainingFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final ITrainingFacade trainingFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTraining(
        @Valid @RequestBody CreateTrainingRequest request
    ) {
        trainingFacade.create(request);
    }

    @GetMapping("/trainee/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TraineeTrainingResponse> getTraineeTrainings(
        @PathVariable String username,
        @RequestParam(required = false) LocalDate from,
        @RequestParam(required = false) LocalDate to,
        @RequestParam(required = false) String trainerUsername,
        @RequestParam(required = false) String trainingTypeName
    ) {
        var request = GetTraineeTrainingsRequest.builder()
            .username(username)
            .from(from)
            .to(to)
            .trainerUsername(trainerUsername)
            .trainingTypeName(trainingTypeName)
            .build();
        return trainingFacade.getTraineeTrainings(request);
    }

    @GetMapping("/trainer/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerTrainingsResponse> getTrainerTrainings(
        @PathVariable String username,
        @RequestParam(required = false) LocalDate from,
        @RequestParam(required = false) LocalDate to,
        @RequestParam(required = false) String traineeUsername
    ) {
        var request = GetTrainerTrainingRequest.builder()
            .username(username)
            .from(from)
            .to(to)
            .traineeUsername(traineeUsername)
            .build();
        return trainingFacade.getTrainerTrainings(request);
    }

    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingTypeResponse> getTrainingTypes() {
        return trainingFacade.getTrainingsTypes();
    }
}
