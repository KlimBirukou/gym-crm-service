package com.epam.gym.crm.controller.rest.training.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateTrainingRequest(

    @NotBlank(message = "Trainee username is required")
    @Schema(
        description = "Existing trainee username",
        example = "Geralt.zRivii"
    )
    String traineeUsername,

    @NotBlank(message = "Trainer username is required")
    @Schema(
        description = "Existing trainer username",
        example = "Vesemir.Oldman"
    )
    String trainerUsername,

    @NotBlank(message = "Training name is required")
    @Schema(
        description = "Training topic name",
        example = "Improve swordsmanship skills"
    )
    String name,

    @NotNull(message = "Training date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "Training date cannot be in the past")
    @Schema(
        description = "Date of planned training",
        example = "2026-06-06"
    )
    LocalDate date,

    @NotNull(message = "Training duration is required")
    @Schema(
        description = "Duration of the planned training in minutes",
        example = "120"
    )
    Integer durationInMinutes
) {

}
