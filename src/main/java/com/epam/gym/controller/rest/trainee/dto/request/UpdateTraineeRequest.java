package com.epam.gym.controller.rest.trainee.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record UpdateTraineeRequest(

    @NotBlank(message = "Trainee first name is required")
    String firstName,
    @NotBlank(message = "Trainee last name is required")
    String lastName,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Birthdate cannot be in future")
    LocalDate birthdate,
    String address
) {
}
