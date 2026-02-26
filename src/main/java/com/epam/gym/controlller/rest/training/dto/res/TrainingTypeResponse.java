package com.epam.gym.controlller.rest.training.dto.res;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TrainingTypeResponse(

    UUID uid,

    String name
) {
}
