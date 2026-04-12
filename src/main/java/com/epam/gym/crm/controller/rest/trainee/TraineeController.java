package com.epam.gym.crm.controller.rest.trainee;

import com.epam.gym.crm.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.crm.facade.trainee.ITraineeFacade;
import com.epam.gym.crm.metrics.annotation.Measured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TraineeController implements ITraineeController {

    private final ITraineeFacade traineeFacade;

    @Override
    @Measured("GET_api_v1_trainee_get")
    public TraineeResponse getTrainee(String username) {
        return traineeFacade.getProfile(username);
    }

    @Override
    @Measured("PUT_api_v1_trainee_update")
    public TraineeResponse updateTrainee(String username, UpdateTraineeRequest request) {
        return traineeFacade.updateTrainee(username, request);
    }

    @Override
    @Measured("PATCH_api_v1_trainee_change_status")
    public void changeStatus(String username, Boolean active) {
        traineeFacade.changeStatus(username, active);
    }

    @Override
    @Measured("DELETE_api_v1_trainee_delete")
    public void deleteTrainee(String username) {
        traineeFacade.delete(username);
    }
}
