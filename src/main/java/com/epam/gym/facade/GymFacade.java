package com.epam.gym.facade;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public final class GymFacade implements IGymFacade {

    private final ITrainerService trainerService;

    @Override
    public Trainer createTrainer(CreateTrainerDto dto) {
        log.info("Creating trainer with firstName = {}, lastName = {}", dto.firstName(), dto.lastName());
        var trainer = trainerService.create(dto);
        log.info("Trainer created with uid = {}", trainer.getUid());
        return trainer;
    }

    @Override
    public void updateTrainer(UpdateTrainerDto dto) {
        log.info("Updating trainer uid = {}", dto.uid());
        trainerService.update(dto);
        log.info("Trainer with uid = {} updated", dto.uid());
    }
}
