package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.TrainingDateConflictException;
import com.epam.gym.exception.TrainingTypeMismatchException;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.active.TrainerNotActiveException;
import com.epam.gym.repository.domain.training.ITrainingRepository;
import com.epam.gym.service.assignment.ITraineeAssignmentTrainerService;
import com.epam.gym.service.assignment.dto.AssignDto;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.training.dto.TrainingsCriteriaDto;
import com.epam.gym.service.type.ITrainingTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService implements ITrainingService {

    private final ITrainingRepository trainingRepository;
    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final ITrainingTypeService trainingTypeService;
    private final ITraineeAssignmentTrainerService traineeAssignmentTrainerService;

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
        traineeAssignmentTrainerService.checkAssign(new AssignDto(dto.traineeUsername(), dto.trainerUsername()));
        var trainingType = trainingTypeService.getByName(dto.type());
        validateTrainingType(trainer, trainingType);
        validateDateAvailability(dto, trainee, trainer);
        var uid = UUID.randomUUID();
        var training = Training.builder()
            .uid(uid)
            .traineeUid(trainee.getUid())
            .trainerUid(trainer.getUid())
            .name(dto.name())
            .trainingType(trainingType)
            .date(dto.date())
            .duration(Duration.ofMinutes(dto.durationInMinutes()))
            .build();
        trainingRepository.save(training);
        return training;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(@NonNull TrainingsCriteriaDto dto) {
        var trainee = traineeService.getByUsername(dto.username());
        var trainingType = trainingTypeService.getByName(dto.trainingType());
        return trainingRepository.getTraineeTrainings(trainee.getUid(), trainingType.getUid()).stream()
            .filter(training -> inDateRange(training.getDate(), dto.from(), dto.to()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(@NonNull TrainingsCriteriaDto dto) {
        var trainer = trainerService.getByUsername(dto.username());
        var trainingType = trainingTypeService.getByName(dto.trainingType());
        return trainingRepository.getTrainerTrainings(trainer.getUid(), trainingType.getUid()).stream()
            .filter(training -> inDateRange(training.getDate(), dto.from(), dto.to()))
            .toList();
    }

    private void validateTrainingType(Trainer trainer, TrainingType trainingType) {
        if (!Objects.equals(trainer.getSpecialization(), trainingType)) {
            throw new TrainingTypeMismatchException(trainer.getUsername(), trainingType.getName());
        }
    }

    private void validateDateAvailability(CreateTrainingDto dto, Trainee trainee, Trainer trainer) {
        trainingRepository.getTrainingsOnDate(dto.date())
            .stream()
            .filter(training ->
                Objects.equals(training.getTraineeUid(), trainee.getUid())
                    || Objects.equals(training.getTrainerUid(), trainer.getUid()))
            .findAny()
            .ifPresent(training -> {
                throw new TrainingDateConflictException(trainee.getUsername(), trainer.getUsername(), dto.date());
            });
    }

    private boolean inDateRange(LocalDate trainingDate, LocalDate from, LocalDate to) {
        return !trainingDate.isBefore(from)
            && !trainingDate.isAfter(to);
    }
}
