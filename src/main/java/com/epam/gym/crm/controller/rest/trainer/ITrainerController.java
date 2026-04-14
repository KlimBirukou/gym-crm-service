package com.epam.gym.crm.controller.rest.trainer;

import com.epam.gym.crm.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TrainerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(
    name = "Trainer",
    description = "Operations with trainer profile"
)
@RequestMapping("/api/v1/trainer")
public interface ITrainerController {

    @Operation(
        summary = "Get trainer profile",
        description = "Return trainer profile by username."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Trainer profile retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TrainerResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Trainer not found",
                value = """
                    {
                      "error": "NOT_FOUND",
                      "description": "Trainer [Night.King] was not found"
                    }
                    """
            )
        )
    )
    @GetMapping(
        path = "/{username}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    TrainerResponse getTrainer(
        @Parameter(
            description = "Trainer username",
            example = "Vesemir.Oldman",
            required = true
        )
        @PathVariable String username
    );

    @Operation(
        summary = "Update trainer profile",
        description = "Update trainer first name, last name."
    )
    @ApiResponse(responseCode = "200",
        description = "Trainer updated successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TrainerResponse.class)
        )
    )
    @ApiResponse(responseCode = "400", description = "Validation failed",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Validation error",
                value = """
                    {
                      "error": "VALIDATION_FAILED",
                      "description": "Trainer first name is required"
                    }
                    """
            ))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Trainer not found",
                value = """
                    {
                      "error": "NOT_FOUND",
                      "description": "Trainee [Night.King] was not found"
                    }
                    """
            )
        )
    )
    @PutMapping(
        path = "/{username}",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    TrainerResponse updateTrainer(
        @Parameter(
            description = "Trainer username",
            example = "Vesemir.Oldman",
            required = true
        )
        @PathVariable String username,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Trainer update request",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UpdateTrainerRequest.class)
            )
        )
        @Valid @RequestBody UpdateTrainerRequest request
    );

    @Operation(
        summary = "Toggle trainer active status",
        description = "Activate or deactivate trainer.")
    @ApiResponse(
        responseCode = "204",
        description = "Status changed successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Trainer not found",
                value = """
                    {
                      "error": "NOT_FOUND",
                      "description": "Trainee [Night.King] was not found"
                    }
                    """
            )
        )
    )
    @PatchMapping("/status/{username}")
    @ResponseStatus(HttpStatus.OK)
    void changeStatus(
        @Parameter(
            description = "Trainer username",
            example = "Vesemir.Oldman",
            required = true
        )
        @PathVariable String username,
        @Parameter(
            description = "Active flag",
            example = "true",
            required = true
        )
        @RequestParam Boolean active
    );
}
