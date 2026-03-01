package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;

public interface IAssignmentService {

    void assign(@NonNull String traineeUsername, @NonNull String trainerUsername);

    boolean checkAssignExist(@NonNull String traineeUsername, @NonNull String trainerUsername);

    List<Trainee> getTrainees(@NonNull String trainerUsername, @NonNull Boolean assigned, @NonNull Boolean active);

    List<Trainer> getTrainers(@NonNull String traineeUsername, @NonNull Boolean assigned, @NonNull Boolean active);
}
