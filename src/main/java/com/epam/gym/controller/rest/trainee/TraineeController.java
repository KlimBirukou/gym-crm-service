package com.epam.gym.controller.rest.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.facade.trainee.ITraineeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TraineeController implements ITraineeController {

    private final ITraineeFacade traineeFacade;

    @Override
    public TraineeResponse getTrainee(String username) {
        return traineeFacade.getProfile(username);
    }

    @Override
    public TraineeResponse updateTrainee(String username, UpdateTraineeRequest request) {
        return traineeFacade.updateTrainee(username, request);
    }

    @Override
    public void changeStatus(String username, Boolean active) {
        traineeFacade.changeStatus(username, active);
    }

    @Override
    public void deleteTrainee(String username) {
        traineeFacade.delete(username);
    }
}
