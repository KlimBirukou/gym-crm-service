package com.epam.gym.crm.client.workload.notifier;

import com.epam.gym.crm.client.workload.ActionType;
import com.epam.gym.crm.client.workload.IWorkloadClient;
import com.epam.gym.crm.client.workload.TrainerWorkloadRequest;
import com.epam.gym.crm.domain.training.Training;
import com.epam.gym.crm.exception.server.WorkloadServerUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerWorkloadNotifier implements ITrainerWorkloadNotifier {

    private final IWorkloadClient workloadClient;

    @Override
    @CircuitBreaker(name = "gym-workload-server", fallbackMethod = "notifyFallback")
    public void notify(@NonNull Training training,
                       @NonNull String trainerUsername,
                       @NonNull ActionType actionType) {
        log.info(
            "Notify workload service. Started. Trainer={}, Duration={}, Date={}, ActionType={}",
            trainerUsername, training.getDuration(), training.getDate(), actionType
        );
        var request = TrainerWorkloadRequest.builder()
            .trainerUsername(trainerUsername)
            .trainingDate(training.getDate())
            .trainingDuration((int) training.getDuration().toMinutes())
            .actionType(actionType)
            .build();
        workloadClient.updateWorkload(request);
        log.info("Notify workload service. Finished. TrainerUsername={}", trainerUsername);
    }

    public void notifyFallback(@NonNull Training training,
                               @NonNull String trainerUsername,
                               @NonNull ActionType actionType,
                               @NonNull Throwable t) {
        log.error(
            "Workload service unavailable. Fallback triggered. Trainer={}, Duration={}, Date={}, ActionType={}",
            trainerUsername, training.getDuration(), training.getDate(), actionType
        );
        throw new WorkloadServerUnavailableException();
    }
}
