package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.trainee.ITraineeRepository;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.repository.training.ITrainingRepository;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class TrainingService implements ITrainingService {

    private ITrainingRepository trainingRepository;
    private ITrainerRepository trainerRepository;
    private ITraineeRepository traineeRepository;

    @Override
    public Training create(CreateTrainingDto dto) {
        log.debug("Creating training for trainer = {}, trainee = {}, on date = {} ",
            dto.trainerUid(), dto.traineeUid(), dto.trainingDate());
        validateNoConflicts(dto);
        var training = Training.builder()
            .trainingUid(UUID.randomUUID())
            .trainerUid(dto.trainerUid())
            .traineeUid(dto.traineeUid())
            .trainingDate(dto.trainingDate())
            .trainingName(dto.trainingName())
            .trainingType(dto.trainingType())
            .trainingDuration(dto.trainingDuration())
            .build();
        trainingRepository.save(training);
        log.debug("Training created with uid = {}", training.getTrainingUid());
        return training;
    }

    private void validateNoConflicts(CreateTrainingDto dto) {
        trainerRepository.findByUid(dto.trainerUid())
            .orElseThrow(() -> new RuntimeException("No trainer with id = %s for create training".formatted(dto.trainerUid())));
        traineeRepository.findByUid(dto.traineeUid())
            .orElseThrow(() -> new RuntimeException("No trainee with id = %s for create training".formatted(dto.traineeUid())));
        var trainings = trainingRepository.findByLocalDate(dto.trainingDate());
        if (trainings.stream()
            .anyMatch(training -> Objects.equals(training.getTrainerUid(), dto.trainerUid()))) {
            throw new RuntimeException(
                "Trainer with id = %s already has a training in this day = %s"
                    .formatted(dto.trainerUid(), dto.trainingDate()));
        }
        if (trainings.stream()
            .anyMatch(training -> Objects.equals(training.getTraineeUid(), dto.traineeUid()))) {
            throw new RuntimeException(
                "Trainee with id = %s already has a training in this day = %s"
                    .formatted(dto.traineeUid(), dto.trainingDate()));
        }
    }

    @Autowired
    public void setTrainingRepository(ITrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Autowired
    private void setTrainerRepository(ITrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    private void setTraineeRepository(ITraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }
}
