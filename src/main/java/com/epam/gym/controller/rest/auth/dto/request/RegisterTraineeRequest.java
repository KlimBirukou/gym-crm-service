package com.epam.gym.controller.rest.auth.dto.request;

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
public record RegisterTraineeRequest(

    @NotBlank(message = "First name is required")
    @Schema(
        description = "First name of potential trainee",
        example = "Vesemir"
    )
    String firstName,
    @NotBlank(message = "Last name is required")
    @Schema(
        description = "Last name of potential trainee",
        example = "Oldman"
    )
    String lastName,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Birthdate cannot be in future")
    @Schema(
        description = "Birth date",
        example = "2000-01-01"
    )
    LocalDate birthdate,
    @Schema(
        description = "Address of potential trainee",
        example = "Kaer Morhen Fortress, Blue Mountains"
    )
    String address
) {

}
