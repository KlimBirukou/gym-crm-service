package com.epam.gym.service.training.dto;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record TrainerTrainingsDto(

    @NonNull String username,
    LocalDate from,
    LocalDate to,
    String traineeUsername
){
}
