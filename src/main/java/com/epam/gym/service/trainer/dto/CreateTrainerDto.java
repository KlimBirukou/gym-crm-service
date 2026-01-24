package com.epam.gym.service.trainer.dto;

public record CreateTrainerDto(

    String firstName,
    String lastName,
    String specialization
) {
}
