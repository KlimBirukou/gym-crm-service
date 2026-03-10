package com.epam.gym.controller.rest.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.facade.auth.IAuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements IAuthController {

    private final IAuthFacade authFacade;

    @Override
    public RegistrationResponse registerTrainee(RegisterTraineeRequest request) {
        return authFacade.registerTrainee(request);
    }

    @Override
    public RegistrationResponse registerTrainer(RegisterTrainerRequest request) {
        return authFacade.registerTrainer(request);
    }

    @Override
    public void login(LoginRequest request) {
        authFacade.login(request);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        authFacade.changePassword(request);
    }
}
