package com.epam.gym.controller.rest.auth;

import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.facade.auth.IAuthFacade;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthFacade authFacade;

    @PostMapping("/registration/trainee")
    public ResponseEntity<@NonNull RegistrationResponse> registerTrainee(
        @Valid @RequestBody RegisterTraineeRequest request
    ) {
        var response = authFacade.registerTrainee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/registration/trainer")
    public ResponseEntity<@NonNull RegistrationResponse> registerTrainer(
        @Valid @RequestBody RegisterTrainerRequest request
    ) {
        var response = authFacade.registerTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull Void> login(
        @Valid @RequestBody LoginRequest request
    ) {
        var result = authFacade.login(request);
        return result
            ? ResponseEntity.status(HttpStatus.OK).build()
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
