package com.epam.gym.service.trainer;

public record CreateTrainerDto(

    String firstName,
    String lastName,
    String specialization
) {
}
