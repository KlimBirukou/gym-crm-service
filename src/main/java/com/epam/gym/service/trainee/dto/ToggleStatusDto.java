package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

import java.util.UUID;

public record ToggleStatusDto(

    @NonNull UUID uid,
    boolean status
) {
}