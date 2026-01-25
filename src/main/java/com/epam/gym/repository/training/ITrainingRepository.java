package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;

import java.util.List;
import java.util.UUID;

public interface ITrainingRepository {

    void save(Training training);

    List<Training> findByTrainerUid(UUID uid);
}
