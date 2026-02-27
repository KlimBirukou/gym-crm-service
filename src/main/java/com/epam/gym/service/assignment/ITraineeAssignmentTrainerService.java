package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;

public interface ITraineeAssignmentTrainerService {

    void assign(@NonNull String traineeUsername, @NonNull String trainerUsername);

    void checkAssignExist(@NonNull String traineeUsername, @NonNull String trainerUsername);

    List<Trainee> getAssignedTrainees(@NonNull String trainerUsername);

    List<Trainer> getAssignedTrainers(@NonNull String traineeUsername);

    List<Trainer> getUnassignedTrainers(@NonNull String traineeUsername);
}
