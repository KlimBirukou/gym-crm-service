package com.epam.gym.mother.dto.trainee;

import com.epam.gym.v1.service.trainee.dto.UpdateTraineeDto;

import java.util.UUID;

public class UpdateTraineeDtoMother {

    public static UpdateTraineeDto get(UUID uid, String address) {
        return new UpdateTraineeDto(
            uid,
            address
        );
    }
}
