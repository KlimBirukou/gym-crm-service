package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.conflict.date.TraineeDateConflictException;
import com.epam.gym.exception.conflict.date.TrainerDateConflictException;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.active.TrainerNotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.repository.domain.training.ITrainingRepository;
import com.epam.gym.service.assignment.IAssignmentService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import com.epam.gym.service.training.dto.TrainerTrainingsDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService implements ITrainingService {

    private final ITrainingRepository trainingRepository;
    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final IAssignmentService traineeAssignmentTrainerService;

    @Override
    @Transactional
    public Training create(@NonNull CreateTrainingDto dto) {
        var trainee = traineeService.getByUsername(dto.traineeUsername());
        if (!trainee.isActive()) {
            throw new TraineeNotActiveException(trainee.getUsername());
        }
        var trainer = trainerService.getByUsername(dto.trainerUsername());
        if (!trainer.isActive()) {
            throw new TrainerNotActiveException(trainer.getUsername());
        }
        if (!traineeAssignmentTrainerService.checkAssignExist(dto.traineeUsername(), dto.trainerUsername())) {
            throw new NotAssignmentException(dto.trainerUsername(), dto.traineeUsername());
        }
        validateDateAvailability(dto, trainee, trainer);
        var uid = UUID.randomUUID();
        var training = Training.builder()
            .uid(uid)
            .traineeUid(trainee.getUid())
            .trainerUid(trainer.getUid())
            .name(dto.name())
            .trainingType(trainer.getSpecialization())
            .date(dto.date())
            .duration(Duration.ofMinutes(dto.durationInMinutes()))
            .build();
        trainingRepository.save(training);
        return training;
    }

    @Override
    @Transactional
    public List<Training> getTraineeTrainings(@NonNull TraineeTrainingsDto dto) {
        var trainee = traineeService.getByUsername(dto.username());
        return trainingRepository.getTraineeTrainings(
            trainee.getUid(),
            dto.from(),
            dto.to(),
            dto.trainerUsername(),
            dto.trainingTypeName()
        );
    }

    @Override
    @Transactional
    public List<Training> getTrainerTrainings(@NonNull TrainerTrainingsDto dto) {
        var trainer = trainerService.getByUsername(dto.username());
        return trainingRepository.getTrainerTrainings(
            trainer.getUid(),
            dto.from(),
            dto.to(),
            dto.traineeUsername()
        );
    }

    private void validateDateAvailability(CreateTrainingDto dto, Trainee trainee, Trainer trainer) {
        trainingRepository.getTrainingsOnDate(dto.date())
            .forEach(training -> {
                if (Objects.equals(training.getTraineeUid(), trainee.getUid())) {
                    throw new TraineeDateConflictException(trainee.getUsername(), dto.date());
                }
                if (Objects.equals(training.getTrainerUid(), trainer.getUid())) {
                    throw new TrainerDateConflictException(trainer.getUsername(), dto.date());
                }
            });
    }
}
