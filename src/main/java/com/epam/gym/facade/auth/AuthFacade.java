package com.epam.gym.facade.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.AuthException;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.user.IUserService;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthFacade implements IAuthFacade {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final IUserService userService;
    private final IPasswordService passwordService;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public RegistrationResponse registerTrainee(@NonNull RegisterTraineeRequest request) {
        log.info("Register trainee. Started. Request={}", request);
        var dto = conversionService.convert(request, CreateTraineeDto.class);
        var trainee = traineeService.create(dto);
        var response = RegistrationResponse.builder()
            .username(trainee.getUsername())
            .password(trainee.getPassword())
            .build();
        log.info("Register trainee. Finished. Response={}", response);
        return response;
    }

    @Override
    @Transactional
    public RegistrationResponse registerTrainer(@NonNull RegisterTrainerRequest request) {
        log.info("Register trainer. Started. Request={}", request);
        var dto = conversionService.convert(request, CreateTrainerDto.class);
        var trainer = trainerService.create(dto);
        var response = RegistrationResponse.builder()
            .username(trainer.getUsername())
            .password(trainer.getPassword())
            .build();
        log.info("Register trainer. Finished. Response={}", response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public void login(@NonNull LoginRequest request) {
        log.info("Login. Started. Username={}", request.username());
        // TODO implement in Spring Security module
        User user = userService.getByUsername(request.username());
        Optional.of(request)
            .filter(r -> passwordService.checkPassword(request.password(), user.getPassword()))
            .orElseThrow(AuthException::new);
        log.info("Login. Finished. Successful. Username={}", request.username());
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordRequest request) {
        log.info("Change password. Started. Username={}", request.username());
        var dto = conversionService.convert(request, ChangePasswordDto.class);
        userService.changePassword(dto);
        log.info("Change password. Finished. Username={}", request.username());
    }
}
