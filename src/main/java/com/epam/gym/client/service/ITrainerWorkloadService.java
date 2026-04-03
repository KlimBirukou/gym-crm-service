package com.epam.gym.client.service;

import com.epam.gym.client.ActionType;
import com.epam.gym.domain.training.Training;
import lombok.NonNull;

public interface ITrainerWorkloadService {

    void notify(@NonNull Training training, @NonNull String trainerUsername, @NonNull ActionType actionType);
}
