package com.epam.gym.repository.domain.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;

import java.util.List;

public interface IAssignmentRepository {

    void assign(@NonNull Trainee trainee, @NonNull Trainer trainer);

    boolean checkAssign(@NonNull String traineeUsername, @NonNull String trainerUsername);

    List<Trainee> getTrainees(@NonNull String trainerUsername,
                             @NonNull Boolean assigned,
                             @NonNull Boolean active);

    List<Trainer> getTrainer(@NonNull String traineeUsername,
                             @NonNull Boolean assigned,
                             @NonNull Boolean active);
}
