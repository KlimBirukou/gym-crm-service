package com.epam.gym.facade.assignment;

import com.epam.gym.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.service.assignment.ITraineeAssignmentTrainerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentFacade implements IAssignmentFacade {

    private final ITraineeAssignmentTrainerService assignmentService;
    private final ConversionService conversionService;

    @Override
    public List<TraineeProfileResponse> getTrainees(@NonNull String username,
                                                    @NonNull Boolean assigned,
                                                    @NonNull Boolean active) {
        log.info("Get trainees. Started. Username={}, assigned={}, active={}", username, assigned, active);
        var response = assignmentService.getTrainees(username, assigned, active)
            .stream()
            .map(trainer -> conversionService.convert(trainer, TraineeProfileResponse.class))
            .toList();
        log.info("Get trainees. Finished. Found {} trainees: {}", response.size(), response);
        return response;
    }

    @Override
    public List<TrainerProfileResponse> getTrainers(@NonNull String username,
                                                    @NonNull Boolean assigned,
                                                    @NonNull Boolean active) {
        log.info("Get trainers. Started. Username={}, assigned={}, active={}", username, assigned, active);
        var response = assignmentService.getTrainers(username, assigned, active)
            .stream()
            .map(trainer -> conversionService.convert(trainer, TrainerProfileResponse.class))
            .toList();
        log.info("Get trainers. Finished. Found {} trainers: {}}", response.size(), response);
        return response;
    }

    @Override
    public void assign(@NonNull AssignRequest request) {
        log.info("Assign. Started. Trainee username={}, trainer username={}",
            request.traineeUsername(), request.trainerUsername());
        assignmentService.assign(request.traineeUsername(), request.trainerUsername());
        log.info("Assign. Finished. Trainee username={}, trainer username={}",
            request.traineeUsername(), request.trainerUsername());
    }
}
