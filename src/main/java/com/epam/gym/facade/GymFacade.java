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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Setter(onMethod_ = @Autowired)
public final class GymFacade implements IGymFacade {

    private ITrainerService trainerService;
    private ITraineeService traineeService;
    private ITrainingService trainingService;

    @Override
    public Trainer createTrainer(@NonNull CreateTrainerDto dto) {
        log.info("Create trainer. Started. FirstName={}, LastName={}",
            dto.firstName(), dto.lastName());
        var trainer = trainerService.create(dto);
        log.info("Create trainer. Finished. FirstName={}, LastName={}, CreatedTrainerUid={}",
            trainer.getFirstName(), trainer.getLastName(), trainer.getUid());
        return trainer;
    }

    @Override
    public void updateTrainer(@NonNull UpdateTrainerDto dto) {
        log.info("Update trainer. Started. UpdatedTrainerUid={}", dto.uid());
        trainerService.update(dto);
        log.info("Update trainer. Finished. UpdatedTrainerUid={}", dto.uid());
    }

    @Override
    public Trainee createTrainee(@NonNull CreateTraineeDto dto) {
        log.info("Create trainee. Started. FirstName={}, LastName={}",
            dto.firstName(), dto.lastName());
        var trainee = traineeService.create(dto);
        log.info("Create trainee. Finished. FirstName={}, LastName={}, CreatedTraineeUid={}",
            trainee.getFirstName(), trainee.getLastName(), trainee.getUid());
        return trainee;
    }

    @Override
    public void updateTrainee(@NonNull UpdateTraineeDto dto) {
        log.info("Update trainee. Started. UpdatedTraineeUid={}", dto.uid());
        traineeService.update(dto);
        log.info("Update trainee. Finished. UpdatedTraineeUid={}", dto.uid());
    }

    @Override
    public void deleteTrainee(@NonNull UUID uid) {
        log.info("Delete trainee. Started. UUID={}", uid);
        traineeService.delete(uid);
        log.info("Delete trainee. Finished. UUID={}", uid);
    }

    @Override
    public Training createTraining(@NonNull CreateTrainingDto dto) {
        log.info("Create training. Started. TrainerUid={}, TraineeUid={}, Date={}",
            dto.trainerUid(), dto.traineeUid(), dto.date());
        var training = trainingService.create(dto);
        log.info("Create training. Finished. TrainerUid={}, TraineeUid={}, Date={}, CreatedTrainingUid={}",
            training.getTrainerUid(), training.getTraineeUid(), training.getDate(), training.getUid());
        return training;
    }
}
