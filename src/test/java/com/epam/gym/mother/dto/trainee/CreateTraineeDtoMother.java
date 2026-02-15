package com.epam.gym.mother.dto.trainee;

import com.epam.gym.v1.service.trainee.dto.CreateTraineeDto;

import java.time.LocalDate;

public class CreateTraineeDtoMother {

    public static CreateTraineeDto get(String firstname, String lastname, String address, LocalDate date) {
        return new CreateTraineeDto(
            firstname,
            lastname,
            address,
            date
        );
    }
}
