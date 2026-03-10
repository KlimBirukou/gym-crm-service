package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.active.TrainerNotActiveException;
import com.epam.gym.repository.domain.assignment.IAssignmentRepository;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AssignmentService implements IAssignmentService {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final IAssignmentRepository assignmentRepository;

    @Override
    @Transactional
    public void assign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        var trainee = Optional.of(traineeUsername)
            .map(traineeService::getByUsername)
            .filter(Trainee::isActive)
            .orElseThrow(() -> new TraineeNotActiveException(traineeUsername));
        var trainer = Optional.of(trainerUsername)
            .map(trainerService::getByUsername)
            .filter(Trainer::isActive)
            .orElseThrow(() -> new TrainerNotActiveException(trainerUsername));
        Optional.of(trainee)
            .filter(Predicate.not(t -> assignmentRepository.checkAssign(traineeUsername, trainerUsername)))
            .orElseThrow(() -> new AlreadyAssignedException(traineeUsername, trainerUsername));
        assignmentRepository.assign(trainee, trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAssignmentExist(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        return assignmentRepository.checkAssign(traineeUsername, trainerUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getTrainees(@NonNull String trainerUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        trainerService.getByUsername(trainerUsername);
        return assignmentRepository.getTrainees(trainerUsername, assigned, active);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getTrainers(@NonNull String traineeUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        traineeService.getByUsername(traineeUsername);
        return assignmentRepository.getTrainers(traineeUsername, assigned, active);
    }
}
