package com.epam.gym.controller.rest.trainee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateTraineeRequest(

    @NotBlank(message = "Trainee first name is required")
    @Schema(
        description = "New trainee first name",
        example = "Vesemir"
    )
    String firstName,
    @NotBlank(message = "Trainee last name is required")
    @Schema(
        description = "New trainee last name",
        example = "Oldman"
    )
    String lastName,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Birthdate cannot be in future")
    @Schema(
        description = "New trainee birthdate",
        example = "2001-01-01"
    )
    LocalDate birthdate,
    @Schema(
        description = "New trainee address",
        example = "Kaer Morhen Fortress, Blue Mountains"
    )
    String address
) {

}
