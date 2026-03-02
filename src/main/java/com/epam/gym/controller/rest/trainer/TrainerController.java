package com.epam.gym.controller.rest.trainer;

import com.epam.gym.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.facade.trainer.ITrainerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerController implements ITrainerController {

    private final ITrainerFacade trainerFacade;

    @Override
    public TrainerResponse getTrainer(String username) {
        return trainerFacade.getProfile(username);
    }

    @Override
    public TrainerResponse updateTrainer(String username, UpdateTrainerRequest request) {
        return trainerFacade.updateTrainer(username, request);
    }

    @Override
    public void changeStatus(String username, Boolean active) {
        trainerFacade.changeStatus(username, active);
    }
}
