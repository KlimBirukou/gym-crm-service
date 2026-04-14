package com.epam.gym.crm.controller.advice;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ErrorDto(
    @NonNull String error,
    @NonNull String description
) {

}
