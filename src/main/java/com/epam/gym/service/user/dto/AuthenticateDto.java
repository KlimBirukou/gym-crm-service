package com.epam.gym.service.user.dto;

import lombok.NonNull;

public record AuthenticateDto(

    @NonNull String username,
    @NonNull String password
) {
}
