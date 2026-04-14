package com.epam.gym.crm.client.workload.notifier;

import com.epam.gym.crm.client.workload.ActionType;
import com.epam.gym.crm.domain.training.Training;
import lombok.NonNull;

public interface ITrainerWorkloadNotifier {

    void notify(@NonNull Training training, @NonNull String trainerUsername, @NonNull ActionType actionType);
}
