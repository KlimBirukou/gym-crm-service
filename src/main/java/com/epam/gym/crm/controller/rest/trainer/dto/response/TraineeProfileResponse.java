package com.epam.gym.crm.controller.rest.trainer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TraineeProfileResponse(

    @Schema(
        description = "Trainee username",
        example = "Vesemi.Oldman"
    )
    @NonNull String username,

    @Schema(
        description = "Trainer first name",
        example = "Vesemir"
    )
    @NonNull String firstName,

    @Schema(
        description = "Trainer last name",
        example = "Oldman"
    )
    @NonNull String lastName
) {

}
