package com.epam.gym.crm.facade.trainee;

import com.epam.gym.crm.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TraineeResponse;
import lombok.NonNull;

public interface ITraineeFacade {

    TraineeResponse getProfile(@NonNull String username);

    TraineeResponse updateTrainee(@NonNull String username, @NonNull UpdateTraineeRequest request);

    void changeStatus(@NonNull String username, boolean active);

    void delete(@NonNull String username);
}
