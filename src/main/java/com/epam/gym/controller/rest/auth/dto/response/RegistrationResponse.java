package com.epam.gym.controller.rest.auth.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record RegistrationResponse(

    @NonNull String username,
    @NonNull String password
) {
}
