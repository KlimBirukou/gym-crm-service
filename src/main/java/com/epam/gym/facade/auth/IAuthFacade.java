package com.epam.gym.facade.auth;

import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import lombok.NonNull;

public interface IAuthFacade {

    RegistrationResponse registerTrainee(@NonNull RegisterTraineeRequest request);

    RegistrationResponse registerTrainer(@NonNull RegisterTrainerRequest request);

    boolean login(@NonNull LoginRequest request);

    void logout();
}
