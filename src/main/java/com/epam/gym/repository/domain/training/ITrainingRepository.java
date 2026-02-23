package com.epam.gym.repository.domain.training;

import com.epam.gym.domain.training.Training;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITrainingRepository {

    void save(@NonNull Training training);

    List<Training> getTraineeTrainings(@NonNull UUID traineeUid, @NonNull UUID trainingTypeUid);

    List<Training> getTrainerTrainings(@NonNull UUID trainerUid, @NonNull UUID trainingTypeUid);

    List<Training> getTrainingsOnDate(@NonNull LocalDate date);
}
