package com.epam.gym.crm.facade.auth;

import com.epam.gym.crm.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.crm.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.crm.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.crm.service.auth.login.ILoginService;
import com.epam.gym.crm.service.trainee.ITraineeService;
import com.epam.gym.crm.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.crm.service.trainer.ITrainerService;
import com.epam.gym.crm.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.crm.service.user.IUserService;
import com.epam.gym.crm.service.user.dto.ChangePasswordDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthFacade implements IAuthFacade {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final IUserService userService;
    private final ILoginService loginService;
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
    @Transactional
    public LoginResponse login(@NonNull LoginRequest request) {
        log.info("Login. Started. Username={}", request.username());
        var response = loginService.login(request);
        log.info("Login. Finished. Username={}", request.username());
        return response;
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
