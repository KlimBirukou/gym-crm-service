package com.epam.gym.mother.dto.trainer;

import com.epam.gym.service.trainer.dto.CreateTrainerDto;

public class CreateTrainerDtoMother {

    public static CreateTrainerDto get(String firstname, String lastname, String specialization) {
        return new CreateTrainerDto(
            firstname,
            lastname,
            specialization
        );
    }
}
