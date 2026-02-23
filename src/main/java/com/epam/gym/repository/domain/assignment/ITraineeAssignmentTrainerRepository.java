package com.epam.gym.repository.domain.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;

public interface ITraineeAssignmentTrainerRepository {

    void assign(@NonNull Trainee trainee, @NonNull Trainer trainer);

    boolean checkAssign(@NonNull String traineeUsername, @NonNull String trainerUsername);

    List<Trainer> getAssignedTrainers(@NonNull String username);

    List<Trainer> getUnassignedTrainers(@NonNull String username);
}
