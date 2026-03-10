package com.epam.gym.controller.rest.training.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TrainingTypeResponse(

    @Schema(
        description = "Training type uuid v4",
        example = "d66e3fde-d7ad-4e35-86ce-e516be124fc6"
    )
    @NonNull UUID uid,

    @Schema(
        description = "Training type name",
        example = "Mortial Arts"
    )
    @NonNull String name
) {

}
