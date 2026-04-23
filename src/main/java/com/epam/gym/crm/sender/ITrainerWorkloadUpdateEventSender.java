package com.epam.gym.crm.sender;

import com.epam.gym.crm.domain.training.Training;
import lombok.NonNull;

public interface ITrainerWorkloadUpdateEventSender {

    void notify(@NonNull Training training, @NonNull String trainerUsername, @NonNull WorkloadUpdateEventType actionType);
}
