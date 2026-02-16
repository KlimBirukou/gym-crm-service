package com.epam.gym.service.user.trainee.dto;

import lombok.NonNull;

public record ChangePasswordDto(

    @NonNull String username,
    @NonNull String oldPassword,
    @NonNull String newPassword
) {
}
