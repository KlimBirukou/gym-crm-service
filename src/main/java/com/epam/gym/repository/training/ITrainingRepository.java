package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;

import java.time.LocalDate;
import java.util.List;

public interface ITrainingRepository {

    void save(Training training);

    List<Training> findByLocalDate(LocalDate date);
}
