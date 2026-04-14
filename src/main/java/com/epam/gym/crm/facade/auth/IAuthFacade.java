package com.epam.gym.crm.facade.auth;

import com.epam.gym.crm.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.crm.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.crm.controller.rest.auth.dto.response.RegistrationResponse;
import lombok.NonNull;

public interface IAuthFacade {

    RegistrationResponse registerTrainee(@NonNull RegisterTraineeRequest request);

    RegistrationResponse registerTrainer(@NonNull RegisterTrainerRequest request);

    LoginResponse login(@NonNull LoginRequest request);

    void changePassword(@NonNull ChangePasswordRequest request);
}
