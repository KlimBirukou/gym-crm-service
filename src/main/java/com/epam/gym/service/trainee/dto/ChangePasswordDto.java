package com.epam.gym.service.trainee.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ChangePasswordDto(

    @NonNull String username,
    @NonNull String oldPassword,
    @NonNull String newPassword
) {
}
