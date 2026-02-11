package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface ITrainingService {

    Training create(@NonNull CreateTrainingDto dto);

    List<Training> findAllByDate(@NonNull LocalDate date);
}
