package com.epam.gym.crm.repository.domain.type;

import com.epam.gym.crm.domain.training.TrainingType;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface ITrainingTypeRepository {

    Optional<TrainingType> getByName(@NonNull String name);

    List<TrainingType> getAll();
}
