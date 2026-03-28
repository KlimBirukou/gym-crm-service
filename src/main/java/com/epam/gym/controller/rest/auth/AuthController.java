package com.epam.gym.controller.rest.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.facade.auth.IAuthFacade;
import com.epam.gym.metrics.annotation.Measured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements IAuthController {

    private final IAuthFacade authFacade;

    @Override
    @Measured("POST_api_v1_auth_register_trainee")
    public RegistrationResponse registerTrainee(RegisterTraineeRequest request) {
        return authFacade.registerTrainee(request);
    }

    @Override
    @Measured("POST_api_v1_auth_register_trainer")
    public RegistrationResponse registerTrainer(RegisterTrainerRequest request) {
        return authFacade.registerTrainer(request);
    }

    @Override
    @Measured("POST_api_v1_auth_login")
    public LoginResponse login(LoginRequest request) {
        return authFacade.login(request);
    }

    @Override
    @Measured("POST_api_v1_auth_logout")
    public void logout() {
    }

    @Override
    @Measured("PUT_api_v1_auth_change_password")
    public void changePassword(ChangePasswordRequest request) {
        authFacade.changePassword(request);
    }
}
