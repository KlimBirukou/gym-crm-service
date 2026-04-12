package com.epam.gym.crm.service.workload;

import com.epam.gym.crm.client.workload.ActionType;
import com.epam.gym.crm.domain.training.Training;
import lombok.NonNull;

public interface ITrainerWorkloadService {

    void notify(@NonNull Training training, @NonNull String trainerUsername, @NonNull ActionType actionType);
}
