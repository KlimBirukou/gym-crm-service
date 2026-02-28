package com.epam.gym.facade.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import lombok.NonNull;

import java.util.List;

public interface ITraineeFacade {

    TraineeResponse getProfile(@NonNull String username);

    List<TrainerProfileResponse> getTrainers(@NonNull String username, @NonNull Boolean assigned);

    TraineeResponse updateTrainee(@NonNull String username, @NonNull UpdateTraineeRequest request);

    void changeStatus(@NonNull String username, boolean active);

    void delete(@NonNull String username);
}
