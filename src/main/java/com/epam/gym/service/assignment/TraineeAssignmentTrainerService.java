package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.domain.assignment.ITraineeAssignmentTrainerRepository;
import com.epam.gym.service.assignment.dto.AssignDto;
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
    public void assign(@NonNull AssignDto dto) {
        var trainee = traineeService.getByUsername(dto.traineeUsername());
        if (!trainee.isActive()) {
            throw new RuntimeException("Trainee must be active");
        }
        var trainer = trainerService.getByUsername(dto.traineeUsername());
        if (!trainer.isActive()) {
            throw new RuntimeException("Trainer must be active");
        }
        checkAssign(dto);
        traineeAssignmentTrainerRepository.assign(trainee, trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkAssign(@NonNull AssignDto dto) {
        if (!traineeAssignmentTrainerRepository.checkAssign(dto.traineeUsername(), dto.trainerUsername())) {
            throw new RuntimeException("Trainer is not assigned to this trainee"); //TODO: should it be a custom exception
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
