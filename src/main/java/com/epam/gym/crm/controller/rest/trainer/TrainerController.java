package com.epam.gym.crm.controller.rest.trainer;

import com.epam.gym.crm.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.crm.facade.trainer.ITrainerFacade;
import com.epam.gym.crm.metrics.annotation.Measured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerController implements ITrainerController {

    private final ITrainerFacade trainerFacade;

    @Override
    @Measured("GET_api_v1_trainer_get")
    public TrainerResponse getTrainer(String username) {
        return trainerFacade.getProfile(username);
    }

    @Override
    @Measured("PUT_api_v1_trainer_update")
    public TrainerResponse updateTrainer(String username, UpdateTrainerRequest request) {
        return trainerFacade.updateTrainer(username, request);
    }

    @Override
    @Measured("PATCH_api_v1_trainer_change_status")
    public void changeStatus(String username, Boolean active) {
        trainerFacade.changeStatus(username, active);
    }
}
