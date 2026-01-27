package com.epam.gym.facade;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.training.ITrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public final class GymFacade implements IGymFacade {

    private final ITrainerService trainerService;
    private final ITraineeService traineeService;
    private final ITrainingService trainingService;

    @Override
    public Trainer createTrainer(@NonNull CreateTrainerDto dto) {
        log.info("Creating trainer with firstName = {}, lastName = {}", dto.firstName(), dto.lastName());
        var trainer = trainerService.create(dto);
        log.info("Trainer created with uid = {}", trainer.getUid());
        return trainer;
    }

    @Override
    public void updateTrainer(@NonNull UpdateTrainerDto dto) {
        log.info("Updating trainer uid = {}", dto.uid());
        trainerService.update(dto);
        log.info("Trainer with uid = {} updated", dto.uid());
    }

    @Override
    public Trainee createTrainee(@NonNull CreateTraineeDto dto) {
        log.info("Creating trainee with firstName = {}, lastName = {}", dto.firstName(), dto.lastName());
        var trainee = traineeService.create(dto);
        log.info("Trainee created with uid = {}", trainee.getUid());
        return trainee;
    }

    @Override
    public void updateTrainee(@NonNull UpdateTraineeDto dto) {
        log.info("Updating trainee uid = {}", dto.uid());
        traineeService.update(dto);
        log.info("Trainee with uid = {} updated", dto.uid());
    }

    @Override
    public void deleteTrainee(@NonNull UUID uid) {
        log.info("Deleting trainee with uid = {}", uid);
        traineeService.delete(uid);
        log.info("Trainee with uid = {} deleted", uid);
    }

    @Override
    public Training createTraining(@NonNull CreateTrainingDto dto) {
        log.info("Creating training for trainer = {}, trainee = {}, on date = {} ",
            dto.trainerUid(), dto.traineeUid(), dto.trainingDate());
        var training = trainingService.create(dto);
        log.info("Training created with uid = {}", training.getTrainingUid());
        return training;
    }
}
