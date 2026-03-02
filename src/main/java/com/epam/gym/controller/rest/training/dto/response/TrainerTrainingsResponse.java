package com.epam.gym.controller.rest.training.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TrainerTrainingsResponse(

    @Schema(
        description = "Training topic name",
        example = "Improve swordsmanship skills"
    )
    @NonNull String name,
    @Schema(
        description = "Date of training",
        example = "2026-06-06"
    )
    @NonNull LocalDate date,
    @Schema(
        description = "Training type name",
        example = "Mortial Arts"
    )
    @NonNull String trainingTypeName,
    @Schema(
        description = "Duration of the training in minutes",
        example = "120"
    )
    int duration,
    @Schema(
        description = "Trainee username",
        example = "Vesemir.Oldman"
    )
    @NonNull String traineeUsername
) {
}
