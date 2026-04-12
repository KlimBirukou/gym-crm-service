package com.epam.gym.crm.facade.assignment;

import com.epam.gym.crm.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TraineeProfileResponse;
import lombok.NonNull;

import java.util.List;

public interface IAssignmentFacade {

    List<TraineeProfileResponse> getTrainees(@NonNull String username,
                                             @NonNull Boolean assigned,
                                             @NonNull Boolean active);

    List<TrainerProfileResponse> getTrainers(@NonNull String username,
                                             @NonNull Boolean assigned,
                                             @NonNull Boolean active);

    void assign(@NonNull AssignRequest request);
}
