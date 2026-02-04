package com.epam.gym.mother.dto.trainer;

import com.epam.gym.service.trainer.dto.UpdateTrainerDto;

import java.util.UUID;

public class UpdateTrainerDtoMother {

    public static UpdateTrainerDto get(UUID uid, String specialization) {
        return new UpdateTrainerDto(
            uid,
            specialization
        );
    }
}
