package com.epam.gym.service.type;

import com.epam.gym.domain.training.TrainingType;
import lombok.NonNull;

import java.util.List;

public interface ITrainingTypeService {

    TrainingType getByName(@NonNull String name);

    List<TrainingType> getAll();
}
