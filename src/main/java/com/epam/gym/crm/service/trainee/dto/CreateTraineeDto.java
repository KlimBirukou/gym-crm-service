package com.epam.gym.crm.service.trainee.dto;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record CreateTraineeDto(

    @NonNull String firstName,
    @NonNull String lastName,
    LocalDate birthdate,
    String address
) {

}
