package com.epam.gym.service.assignment.dto;

import lombok.NonNull;

public record AssignDto(

    @NonNull String traineeUsername,
    @NonNull String trainerUsername
) {
}
