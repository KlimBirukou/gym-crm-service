package com.epam.gym.controller.rest.trainer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TrainerResponse(

    @Schema(
        description = "Trainer username",
        example = "Vesemir.Oldman"
    )
    @NonNull String username,
    @Schema(
        description = "Trainer first name",
        example = "Vesemir"
    )
    @NonNull String firstName,
    @Schema(
        description = "Trainee last name",
        example = "Oldman"
    )
    @NonNull String lastName,
    @Schema(
        description = "Trainer specialization name",
        example = "Martial Arts"
    )
    @NonNull String specialization,
    @Schema(
        description = "Trainer account status",
        example = "true"
    )
    @NonNull Boolean active,
    @Schema(
        description = "Trainer assigned trainees"
    )
    @NonNull List<TraineeProfileResponse> trainees
) {

}
