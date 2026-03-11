package com.epam.gym.controller.rest.assignment;

import com.epam.gym.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.facade.assignment.IAssignmentFacade;
import com.epam.gym.metrics.annotation.Measured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssignmentController implements IAssignmentController {

    private final IAssignmentFacade assignmentFacade;

    @Override
    @Measured("GET_api_v1_assignments_get_trainees")
    public List<TraineeProfileResponse> getTrainees(String username,
                                                    Boolean assigned,
                                                    Boolean active
    ) {
        return assignmentFacade.getTrainees(username, assigned, active);
    }

    @Override
    @Measured("GET_api_v1_assignments_get_trainers")
    public List<TrainerProfileResponse> getTrainers(String username,
                                                    Boolean assigned,
                                                    Boolean active
    ) {
        return assignmentFacade.getTrainers(username, assigned, active);
    }

    @Override
    @Measured("POST_api_v1_assignments_assign")
    public void assign(AssignRequest request) {
        assignmentFacade.assign(request);
    }
}
