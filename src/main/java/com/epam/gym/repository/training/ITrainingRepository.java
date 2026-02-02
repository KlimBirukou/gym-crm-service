package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITrainingRepository {

    void save(@NonNull Training training);

    List<Training> findByLocalDate(@NonNull LocalDate date);

    List<Training> findByTraineeUid(@NonNull UUID uid);
}
