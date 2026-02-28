package com.epam.gym.facade.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.assignment.ITraineeAssignmentTrainerService;
import com.epam.gym.service.trainee.TraineeService;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeFacade implements ITraineeFacade {

    private final ITraineeAssignmentTrainerService assignmentService;
    private final TraineeService traineeService;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public TraineeResponse getProfile(@NonNull String username) {
        log.info("Get trainee. Started. Username={}", username);
        var trainee = traineeService.getByUsername(username);
        var response = buildTraineeResponse(trainee);
        log.info("Get trainee. Finished. Response={}", response);
        return response;
    }

    @Override
    public List<TrainerProfileResponse> getTrainers(@NonNull String username, @NonNull Boolean assigned) {
        log.info("Get trainers: username={}, assigned={}", username, assigned);
        var trainers = assignmentService.getTrainers(username, assigned, true);
        var response = mapToTrainerProfiles(trainers);
        log.info("Found {} trainers: username={}, assigned={}", response.size(), username, assigned);
        return response;
    }

    @Override
    @Transactional
    public TraineeResponse updateTrainee(@NonNull String username, @NonNull UpdateTraineeRequest request) {
        log.info("Update trainee. Started. Request={}", request);
        var dto = UpdateTraineeDto.builder()
            .username(username)
            .firstName(request.firstName())
            .lastName(request.lastName())
            .birthdate(request.birthdate())
            .address(request.address())
            .build();
        var trainee = traineeService.update(dto);
        var response = buildTraineeResponse(trainee);
        log.info("Update trainee. Finished. Response={}", response);
        return response;
    }

    @Override
    @Transactional
    public void changeStatus(@NonNull String username, boolean active) {
        log.info("Change trainee status. Started. Username={}, status={}", username, active);
        traineeService.changeStatus(username, active);
        log.info("Change trainee status. Finished. Username={}, status={}", username, active);
    }

    @Override
    @Transactional
    public void delete(@NonNull String username) {
        log.info("Delete trainer. Started. Username={}", username);
        traineeService.delete(username);
        log.info("Delete trainer. Finished. Username={}", username);
    }

    private TraineeResponse buildTraineeResponse(Trainee trainee) {
        return TraineeResponse.builder()
            .username(trainee.getUsername())
            .firstName(trainee.getFirstName())
            .lastName(trainee.getLastName())
            .birthdate(trainee.getBirthdate())
            .address(trainee.getAddress())
            .active(trainee.isActive())
            .trainers(getAssignedActiveTrainers(trainee.getUsername()))
            .build();
    }

    private List<TrainerProfileResponse> getAssignedActiveTrainers(String username) {
        var trainers = assignmentService.getTrainers(username, true, true);
        return mapToTrainerProfiles(trainers);
    }

    private List<TrainerProfileResponse> mapToTrainerProfiles(List<Trainer> trainers) {
        return trainers.stream()
            .map(trainer -> conversionService.convert(trainer, TrainerProfileResponse.class))
            .toList();
    }
}
