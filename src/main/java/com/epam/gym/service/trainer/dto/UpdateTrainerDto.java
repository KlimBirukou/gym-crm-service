package com.epam.gym.service.trainer.dto;

import java.util.UUID;

public record UpdateTrainerDto (

    UUID uid,
    String specialization
) {
}
