package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;

public interface ITraineeAssignmentTrainerService {

    void assign(@NonNull String traineeUsername, @NonNull String trainerUsername);

    void checkAssignExist(@NonNull String traineeUsername, @NonNull String trainerUsername);

    List<Trainee> getTrainees(@NonNull String trainerUsername, @NonNull Boolean assigned, @NonNull Boolean active);

    List<Trainer> getTrainers(@NonNull String traineeUsername, @NonNull Boolean assigned, @NonNull Boolean active);
}
