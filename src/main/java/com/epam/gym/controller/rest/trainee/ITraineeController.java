package com.epam.gym.controller.rest.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(
    name = "Trainee",
    description = "Operations with trainee profile"
)
@RequestMapping("/api/v1/trainee")
public interface ITraineeController {

    @Operation(
        summary = "Get trainee profile",
        description = "Return trainee profile by username."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Trainee profile retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TraineeResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainee not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Trainee not found",
                value = """
                    {
                      "error": "NOT_FOUND",
                      "description": "Trainee [Night.King] was not found"
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
    TraineeResponse getTrainee(
        @Parameter(
            description = "Trainee username",
            example = "Arya.Stark",
            required = true
        )
        @PathVariable String username
    );

    @Operation(
        summary = "Update trainee profile",
        description = "Update trainee first name, last name, address, birthdate."
    )
    @ApiResponse(responseCode = "200",
        description = "Trainee updated successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TraineeResponse.class)
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
                      "description": "Trainee first name is required"
                    }
                    """
            ))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainee not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Trainee not found",
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
    TraineeResponse updateTrainee(
        @Parameter(
            description = "Trainee username",
            example = "Arya.Stark",
            required = true
        )
        @PathVariable String username,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Trainee update request",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UpdateTraineeRequest.class)
            )
        )
        @Valid @RequestBody UpdateTraineeRequest request
    );

    @Operation(
        summary = "Toggle trainee active status",
        description = "Activate or deactivate trainee.")
    @ApiResponse(
        responseCode = "204",
        description = "Status changed successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainee not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Trainee not found",
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
            description = "Trainee username",
            example = "Arya.Stark",
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

    @Operation(
        summary = "Delete trainee",
        description = "Delete trainee by username."
    )
    @ApiResponse(
        responseCode = "204",
        description = "Trainee deleted"
    )
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    void deleteTrainee(
        @Parameter(
            description = "Trainee username",
            example = "Arya.Stark",
            required = true
        )
        @PathVariable String username
    );
}
