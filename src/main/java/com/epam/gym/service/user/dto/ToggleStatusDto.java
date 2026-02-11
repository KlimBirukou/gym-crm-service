package com.epam.gym.service.user.dto;

import lombok.NonNull;

public record ToggleStatusDto(

    @NonNull String username,
    boolean status
) {
}
