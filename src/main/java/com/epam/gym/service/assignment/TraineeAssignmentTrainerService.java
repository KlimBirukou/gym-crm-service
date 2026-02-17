package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.active.TrainerNotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
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
public class TraineeAssignmentTrainerService implements ITraineeAssignmentTrainerService {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final ITraineeAssignmentTrainerRepository traineeAssignmentTrainerRepository;

    @Override
    @Transactional
    public void assign(@NonNull AssignDto dto) {
        var trainee = traineeService.getByUsername(dto.traineeUsername());
        if (!trainee.isActive()) {
            throw new TraineeNotActiveException(trainee.getUsername());
        }
        var trainer = trainerService.getByUsername(dto.traineeUsername());
        if (!trainer.isActive()) {
            throw new TrainerNotActiveException(trainer.getUsername());
        }
        checkAssign(dto);
        traineeAssignmentTrainerRepository.assign(trainee, trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkAssign(@NonNull AssignDto dto) {
        if (!traineeAssignmentTrainerRepository.checkAssign(dto.traineeUsername(), dto.trainerUsername())) {
            throw new NotAssignmentException(dto.trainerUsername(), dto.traineeUsername());
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
