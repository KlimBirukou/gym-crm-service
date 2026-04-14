package com.epam.gym.crm.facade.trainer;

import com.epam.gym.crm.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TrainerResponse;
import lombok.NonNull;

public interface ITrainerFacade {

    TrainerResponse getProfile(@NonNull String username);

    TrainerResponse updateTrainer(@NonNull String username, @NonNull UpdateTrainerRequest request);

    void changeStatus(@NonNull String username, boolean active);
}
