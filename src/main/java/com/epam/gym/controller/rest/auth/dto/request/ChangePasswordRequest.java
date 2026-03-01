package com.epam.gym.controller.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(

    @NotBlank(message = "Username is required")
    String username,
    @NotBlank(message = "Old password is required")
    String oldPassword,
    @NotBlank(message = "New password is required")
    String newPassword
) {
}
