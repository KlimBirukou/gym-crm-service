package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.domain.assignment.ITraineeAssignmentTrainerRepository;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraineeAssignmentTrainerService implements ITraineeAssignmentTrainerService{

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final ITraineeAssignmentTrainerRepository traineeAssignmentTrainerRepository;

    @Override
    @Transactional
    public void assign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        var trainee = traineeService.getByUsername(traineeUsername);
        var trainer = trainerService.getByUsername(trainerUsername);
        checkAssign(traineeUsername, trainerUsername);
        traineeAssignmentTrainerRepository.assign(trainee, trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkAssign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        if (!traineeAssignmentTrainerRepository.checkAssign(traineeUsername, trainerUsername)) {
            throw new RuntimeException("Trainer is not assigned to this trainee");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getAssignedTrainers(@NonNull String username) {
        return traineeAssignmentTrainerRepository.getAssignedTrainers(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(@NonNull String username) {
        return traineeAssignmentTrainerRepository.getUnassignedTrainers(username);
    }
}
