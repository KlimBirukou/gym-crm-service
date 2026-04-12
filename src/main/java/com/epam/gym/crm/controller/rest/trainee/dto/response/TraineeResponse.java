package com.epam.gym.crm.controller.rest.trainee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TraineeResponse(

    @Schema(
        description = "Trainee username",
        example = "Vesemir.Oldman"
    )
    @NonNull String username,

    @Schema(
        description = "Trainee first name",
        example = "Vesemir"
    )
    @NonNull String firstName,

    @Schema(
        description = "Trainee last name",
        example = "Oldman"
    )
    @NonNull String lastName,

    @Schema(
        description = "Trainee birthdate",
        example = "2000-01-01"
    )
    @NonNull LocalDate birthdate,

    @Schema(
        description = "Trainee address",
        example = "Kaer Morhen Fortress, Blue Mountains"
    )
    @NonNull String address,

    @Schema(
        description = "Trainee account status",
        example = "true"
    )
    @NonNull Boolean active,

    @Schema(
        description = "Trainee assigned trainers"
    )
    @NonNull List<TrainerProfileResponse> trainers
) {

}
