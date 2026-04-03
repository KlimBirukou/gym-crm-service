package com.epam.gym.client.service;

import com.epam.gym.client.ActionType;
import com.epam.gym.client.IWorkloadClient;
import com.epam.gym.client.TrainerWorkloadRequest;
import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerWorkloadService implements ITrainerWorkloadService {

    private static final String WORKLOAD_CB = "workloadService";

    private final IWorkloadClient workloadClient;

    @Override
    @CircuitBreaker(name = WORKLOAD_CB, fallbackMethod = "notifyFallback")
    public void notify(@NonNull Training training, @NonNull String trainerUsername, @NonNull ActionType actionType) {
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

    private void notifyFallback(@NonNull Training training,
                                @NonNull String trainerUsername,
                                @NonNull ActionType actionType,
                                Throwable cause) {
        log.error(
            "Workload service unavailable. Fallback triggered. TrainerUsername={}, ActionType={}, Cause={}",
            trainerUsername, actionType, cause.getMessage()
        );
    }
}
