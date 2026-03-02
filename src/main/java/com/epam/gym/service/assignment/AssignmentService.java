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

@Service
@RequiredArgsConstructor
public class AssignmentService implements IAssignmentService {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final IAssignmentRepository traineeAssignmentTrainerRepository;

    @Override
    @Transactional
    public void assign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        var trainee = traineeService.getByUsername(traineeUsername);
        if (!trainee.isActive()) {
            throw new TraineeNotActiveException(trainee.getUsername());
        }
        var trainer = trainerService.getByUsername(trainerUsername);
        if (!trainer.isActive()) {
            throw new TrainerNotActiveException(trainer.getUsername());
        }
        if (traineeAssignmentTrainerRepository.checkAssign(traineeUsername, trainerUsername)) {
            throw new AlreadyAssignedException(traineeUsername, trainerUsername);
        }
        traineeAssignmentTrainerRepository.assign(trainee, trainer);
    }

    @Override
    @Transactional
    public boolean checkAssignExist(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        return traineeAssignmentTrainerRepository.checkAssign(traineeUsername, trainerUsername);
    }

    @Override
    @Transactional
    public List<Trainee> getTrainees(@NonNull String trainerUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        trainerService.getByUsername(trainerUsername);
        return traineeAssignmentTrainerRepository.getTrainees(trainerUsername, assigned, active);
    }

    @Override
    @Transactional
    public List<Trainer> getTrainers(@NonNull String traineeUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        traineeService.getByUsername(traineeUsername);
        return traineeAssignmentTrainerRepository.getTrainer(traineeUsername, assigned, active);
    }
}
