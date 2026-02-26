package com.epam.gym.controlller.rest.training;

import com.epam.gym.controlller.rest.training.dto.req.CreateTrainingRequest;
import com.epam.gym.controlller.rest.training.dto.req.GetTraineeTrainingsRequest;
import com.epam.gym.controlller.rest.training.dto.req.GetTrainerTrainingRequest;
import com.epam.gym.controlller.rest.training.dto.res.TraineeTrainingResponse;
import com.epam.gym.controlller.rest.training.dto.res.TrainerTrainingsResponse;
import com.epam.gym.controlller.rest.training.dto.res.TrainingTypeResponse;
import com.epam.gym.facade.training.ITrainingFacade;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final ITrainingFacade trainingFacade;

    @PostMapping
    public ResponseEntity<@NonNull Void> createTraining(
        @Valid @RequestBody CreateTrainingRequest request
    ) {
        trainingFacade.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trainee/username/{username}")
    public ResponseEntity<@NonNull List<TraineeTrainingResponse>> getTraineeTrainings(
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
        var trainings = trainingFacade.getTraineeTrainings(request);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/trainer/username/{username}")
    public ResponseEntity<@NonNull List<TrainerTrainingsResponse>> getTrainerTrainings(
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
        var trainings = trainingFacade.getTrainerTrainings(request);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/types")
    public ResponseEntity<@NonNull List<TrainingTypeResponse>> getTrainingTypes() {
        var types = trainingFacade.getTrainingsTypes();
        return ResponseEntity.ok(types);
    }
}
