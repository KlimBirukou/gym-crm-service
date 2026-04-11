package com.epam.gym.client.workload.service;

import com.epam.gym.client.workload.ActionType;
import com.epam.gym.client.workload.IWorkloadClient;
import com.epam.gym.client.workload.TrainerWorkloadRequest;
import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.server.WorkloadServerUnavailableException;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.ITrainingService;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerWorkloadService implements ITrainerWorkloadService {

    private static final String WORKLOAD_CB = "gym-workload-server";

    private final ITrainingService trainingService;
    private final ITrainerService trainerService;
    private final Clock clock;
    private final IWorkloadClient workloadClient;

    @Override
    @CircuitBreaker(name = WORKLOAD_CB, fallbackMethod = "notifyFallback")
    public void notify(@NonNull Training training,
                       @NonNull String trainerUsername,
                       @NonNull ActionType actionType) {
        log.info(
            "Notify workload service. Trainer={}, Duration={}, Date={}, ActionType={}",
            trainerUsername, training.getDuration(), training.getDate(), actionType
        );
        var request = TrainerWorkloadRequest.builder()
            .trainerUsername(trainerUsername)
            .trainingDate(training.getDate())
            .trainingDuration((int) training.getDuration().toMinutes())
            .actionType(actionType)
            .build();
        workloadClient.updateWorkload(request);
        log.info("Notify workload service. Done. TrainerUsername={}", trainerUsername);
    }

    @Override
    public void processMany(@NonNull String username) {
        var dto = TraineeTrainingsDto.builder()
            .username(username)
            .from(LocalDate.now(clock))
            .to(null)
            .trainerUsername(null)
            .trainingTypeName(null)
            .build();
        log.info("DTO -------------, {}", dto);
        var trainings = trainingService.getTraineeTrainings(dto);
        log.info("TRAININGS -------, {}", trainings);
        var trainerUidToUsername = trainerService.getByUids(
                trainings.stream()
                    .map(Training::getTrainerUid)
                    .distinct()
                    .toList()
            )
            .stream()
            .collect(Collectors.toMap(Trainer::getUid, Trainer::getUsername));
        log.info("TRAINER ---------, {}", trainerUidToUsername);
        trainings.forEach(training ->
            notify(training, trainerUidToUsername.get(training.getTrainerUid()), ActionType.DELETE)
        );
    }

    public void notifyFallback(@NonNull Training training,
                                @NonNull String trainerUsername,
                                @NonNull ActionType actionType, Throwable t) {
        log.error(
            "Workload service unavailable. Fallback triggered. Trainer={}, Duration={}, Date={}, ActionType={}",
            trainerUsername, training.getDuration(), training.getDate(), actionType
        );
        throw new WorkloadServerUnavailableException();
    }
}
