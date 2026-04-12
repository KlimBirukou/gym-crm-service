package com.epam.gym.crm.service.trainer.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateTrainerDto(

    @NonNull String username,
    @NonNull String firstName,
    @NonNull String lastName
) {

}
