package com.epam.gym.service.trainee.dto;

import lombok.NonNull;

public record ChangePasswordDto(

    @NonNull String username,
    @NonNull String oldPassword,
    @NonNull String newPassword
) {
}
