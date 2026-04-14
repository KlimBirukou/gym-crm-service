package com.epam.gym.crm.service.trainer.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateTrainerDto(

    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String specialization
) {

}
