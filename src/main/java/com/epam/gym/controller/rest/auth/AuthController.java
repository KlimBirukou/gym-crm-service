package com.epam.gym.controller.rest.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.facade.auth.IAuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthFacade authFacade;

    @PostMapping("/registration/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse registerTrainee(
        @Valid @RequestBody RegisterTraineeRequest request
    ) {
        return authFacade.registerTrainee(request);
    }

    @PostMapping("/registration/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse registerTrainer(
        @Valid @RequestBody RegisterTrainerRequest request
    ) {
        return authFacade.registerTrainer(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(
        @Valid @RequestBody LoginRequest request
    ) {
        authFacade.login(request);
    }

    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        authFacade.changePassword(request);
    }
}
