package com.epam.gym.service.trainee.dto;

import java.util.UUID;

public record UpdateTraineeDto(

    UUID uid,
    String address
) {
}
