package com.epam.gym.facade.trainer;

import com.epam.gym.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.controller.rest.trainer.dto.response.ShortUserProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.assignment.ITraineeAssignmentTrainerService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerFacade implements ITrainerFacade {

    private final ITraineeAssignmentTrainerService assignmentService;
    private final ITrainerService trainerService;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public TrainerResponse getProfile(@NonNull String username) {
        log.info("Get trainer. Started. Username={}", username);
        var trainer = trainerService.getByUsername(username);
        var response = buildTrainerResponse(username, trainer);
        log.info("Get trainer. Finished. Response={}", response);
        return response;
    }

    @Override
    @Transactional
    public TrainerResponse updateTrainer(@NonNull String username, @NonNull UpdateTrainerRequest request) {
        log.info("Update trainer. Started. Request={}", request);
        var dto = UpdateTrainerDto.builder()
            .username(username)
            .firstName(request.firstName())
            .lastName(request.lastName())
            .build();
        var trainer = trainerService.update(dto);
        var response = buildTrainerResponse(dto.username(), trainer);
        log.info("Update trainer. Finished. Response={}", response);
        return response;
    }

    @Override
    @Transactional
    public void changeStatus(@NonNull String username, boolean active) {
        log.info("Change trainer status. Started. Username={}, status={}", username, active);
        trainerService.changeStatus(username, active);
        log.info("Change trainer status. Finished. Username={}, status={}", username, active);
    }

    private TrainerResponse buildTrainerResponse(String username, Trainer trainer) {
        return TrainerResponse.builder()
            .username(trainer.getUsername())
            .firstName(trainer.getFirstName())
            .lastName(trainer.getLastName())
            .specialization(trainer.getSpecialization().getName())
            .active(trainer.isActive())
            .trainees(buildTraineesList(username))
            .build();
    }

    private List<ShortUserProfileResponse> buildTraineesList(String username) {
        return assignmentService.getAssignedTrainees(username)
            .stream()
            .map(trainee -> conversionService.convert(trainee, ShortUserProfileResponse.class))
            .toList();
    }
}
