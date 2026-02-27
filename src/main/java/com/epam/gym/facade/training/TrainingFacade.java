package com.epam.gym.facade.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.User;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.ITrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import com.epam.gym.service.training.dto.TrainerTrainingsDto;
import com.epam.gym.service.type.ITrainingTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingFacade implements ITrainingFacade {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final ITrainingService trainingService;
    private final ITrainingTypeService trainingTypeService;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public void create(@NonNull CreateTrainingRequest request) {
        log.info("Create training. Started. Request={}", request);
        var dto = conversionService.convert(request, CreateTrainingDto.class);
        var training = trainingService.create(dto);
        log.info("Create trainings. Finished. Training={}", training);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingResponse> getTraineeTrainings(@NonNull GetTraineeTrainingsRequest request) {
        log.info("Get trainee trainings. Started. Request={}", request);
        var dto = conversionService.convert(request, TraineeTrainingsDto.class);
        var trainings = trainingService.getTraineeTrainings(dto);
        var trainersMap = trainings.stream()
            .map(Training::getTrainerUid)
            .distinct()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this::collectTrainerUidToUsername
            ));
        var response = trainings.stream()
            .map(item -> TraineeTrainingResponse.builder()
                .name(item.getName())
                .date(item.getDate())
                .trainingTypeName(item.getTrainingType().getName())
                .duration((int) item.getDuration().toMinutes())
                .trainerUsername(trainersMap.get(item.getTrainerUid()))
                .build())
            .toList();
        log.info("Get trainee trainings. Finished. Found {} records: {}", response.size(), response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingsResponse> getTrainerTrainings(@NonNull GetTrainerTrainingRequest request) {
        log.info("Get trainer trainings. Started. Request={}", request);
        var dto = conversionService.convert(request, TrainerTrainingsDto.class);
        var trainings = trainingService.getTrainerTrainings(dto);
        var traineesMap = trainings.stream()
            .map(Training::getTraineeUid)
            .distinct()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this::collectTraineeUidToUsername
            ));
        var response = trainings.stream()
            .map(item -> TrainerTrainingsResponse.builder()
                .name(item.getName())
                .date(item.getDate())
                .trainingTypeName(item.getTrainingType().getName())
                .duration((int) item.getDuration().toMinutes())
                .traineeUsername(traineesMap.get(item.getTraineeUid()))
                .build())
            .toList();
        log.info("Get trainer trainings. Finished. Found {} records: {}", response.size(), response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeResponse> getTrainingsTypes() {
        log.info("Get all trainings types. Started.");
        var response = trainingTypeService.getAll()
            .stream()
            .map(item -> conversionService.convert(item, TrainingTypeResponse.class))
            .toList();
        log.info("Get all trainings types. Finished. Found {} records: \n{}", response.size(), response);
        return response;
    }

    private Map<UUID, String> collectTraineeUidToUsername(List<UUID> traineeUids) {
        return collectUserUidToUsername(traineeUids, traineeService::getByUids);
    }

    private Map<UUID, String> collectTrainerUidToUsername(List<UUID> trainerUids) {
        return collectUserUidToUsername(trainerUids, trainerService::getByUids);
    }

    private <T extends User> Map<UUID, String> collectUserUidToUsername(List<UUID> traineeUids,
                                                                        Function<List<UUID>, List<T>> fetcher) {
        return fetcher.apply(traineeUids)
            .stream()
            .collect(Collectors.toMap(User::getUid, User::getUsername));
    }
}
