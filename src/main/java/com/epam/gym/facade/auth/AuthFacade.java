package com.epam.gym.facade.auth;

import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.domain.user.User;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthFacade implements IAuthFacade {

    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final ConversionService conversionService;
    private final IPasswordService passwordService;

    @Override
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
    public boolean login(@NonNull LoginRequest request) {
        log.info("Login. Started. Username={}", request.username());
        User user;
        try {
            user = traineeService.getByUsername(request.username());
        } catch (Exception e) {
            user = trainerService.getByUsername(request.username());
        }
        log.info("Login. Finished. Username={}", request.username());
        return passwordService.checkPassword(request.password(), user.getPassword());
        // TODO implement in Spring Security module
    }

    @Override
    public void logout() {
        log.info("Logout. Started.");
        // TODO implement in Spring Security module
        log.info("Logout. Finished.");
    }
}
