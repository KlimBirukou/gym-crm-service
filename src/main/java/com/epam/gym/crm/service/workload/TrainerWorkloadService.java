package com.epam.gym.crm.service.workload;

import com.epam.gym.crm.client.workload.ActionType;
import com.epam.gym.crm.client.workload.notifier.ITrainerWorkloadNotifier;
import com.epam.gym.crm.domain.training.Training;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.service.trainer.ITrainerService;
import com.epam.gym.crm.service.training.ITrainingService;
import com.epam.gym.crm.service.training.dto.TraineeTrainingsDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerWorkloadService implements ITrainerWorkloadService {

    private final ITrainingService trainingService;
    private final ITrainerService trainerService;
    private final Clock clock;
    private final ITrainerWorkloadNotifier workloadService;

    @Override
    @Transactional(readOnly = true)
    public void processMany(@NonNull String username) {
        var trainings = trainingService.getTraineeTrainings(TraineeTrainingsDto.builder()
            .username(username)
            .from(LocalDate.now(clock))
            .build());
        var trainerUidToUsernameMapping = fetchTrainingsTrainerData(trainings)
            .collect(Collectors.toMap(Trainer::getUid, Trainer::getUsername));
        trainings.forEach(t ->
            workloadService.notify(t, trainerUidToUsernameMapping.get(t.getTrainerUid()), ActionType.DELETE)
        );
    }

    private Stream<Trainer> fetchTrainingsTrainerData(List<Training> trainings) {
        return trainings.stream()
            .map(Training::getTrainerUid)
            .distinct()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                trainerService::getByUids
            ))
            .stream();
    }
}
