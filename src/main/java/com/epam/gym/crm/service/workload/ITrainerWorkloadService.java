package com.epam.gym.crm.service.workload;

import lombok.NonNull;

public interface ITrainerWorkloadService {

    void processMany(@NonNull String traineeUsername);
}
