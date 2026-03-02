package com.epam.gym.controller.rest.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
    name = "Training",
    description = "Operations with trainings"
)
@RequestMapping("/v1/training")
public interface ITrainingController {

    @Operation(
        summary = "Create training",
        description = "Create a new training record."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Training created successfully"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request - validation failed",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Validation error",
                value = """
                    {
                      "error": "VALIDATION_FAILED",
                      "description": "Trainee username is required; Trainer username is required"
                    }
                    """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Trainee or trainer not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = {
                @ExampleObject(
                    name = "Not found error",
                    value = """
                        {
                          "error": "NOT_FOUND",
                          "description": "Trainee [Night.King] was not found"
                        }
                        """
                ),
                @ExampleObject(
                    name = "Not found error",
                    value = """
                        {
                          "error": "NOT_FOUND",
                          "description": "Trainer [Night.King] was not found"
                        }
                        """
                )
            }
        )
    )
    @ApiResponse(
        responseCode = "409",
        description = "Date conflict - user already has training on this date",
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Trainee date conflict",
                    value = """
                    {
                      "error": "CONFLICT",
                      "description": "Trainee [Arya.Stark] already has a training on [2026-03-15] date"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Trainer date conflict",
                    value = """
                    {
                      "error": "CONFLICT",
                      "description": "Trainer [Syrio.Forel] already has a training on [2026-03-15] date"
                    }
                    """
                )
            }
        )
    )
    @ApiResponse(
        responseCode = "422",
        description = "Business logic error",
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Not assigned",
                    value = """
                    {
                      "error": "UNPROCESSABLE_CONTENT",
                      "description": "Trainer [Arya.Stark] not assigned to [Master.Yoda] trainee"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Trainer not active",
                    value = """
                    {
                      "error": "UNPROCESSABLE_CONTENT",
                      "description": "Trainer [John.Wick] not active to perform this action"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Trainee not active",
                    value = """
                    {
                      "error": "UNPROCESSABLE_CONTENT",
                      "description": "Trainee [Brienne.Tarth] not active to perform this action"
                    }
                    """
                )
            }
        )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createTraining(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Training creation details",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CreateTrainingRequest.class)
            )
        )
        @Valid @RequestBody CreateTrainingRequest request
    );

    @Operation(summary = "Get trainee trainings", description = "Retrieve list of trainings for a specific trainee.")
    @ApiResponse(
        responseCode = "200",
        description = "List of trainee trainings retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = TraineeTrainingResponse.class))
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
    @GetMapping("/trainee/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    List<TraineeTrainingResponse> getTraineeTrainings(
        @Parameter(
            description = "Trainee username",
            example = "Arya.Stark",
            required = true
        )
        @PathVariable String username,
        @Parameter(
            description = "Filter from date",
            example = "2026-01-01"
        )
        @RequestParam(required = false) LocalDate from,
        @Parameter(
            description = "Filter to date",
            example = "2027-01-01"
        )
        @RequestParam(required = false) LocalDate to,
        @Parameter(
            description = "Filter by trainer username",
            example = "Syrio.Forel"
        )
        @RequestParam(required = false) String trainerUsername,
        @Parameter(
            description = "Filter by training type",
            example = "Fencing"
        )
        @RequestParam(required = false) String trainingTypeName
    );

    @Operation(
        summary = "Get trainer trainings",
        description = "Retrieve list of trainings for a specific trainer."
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of trainer trainings retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = TrainerTrainingsResponse.class)
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
    @GetMapping("/trainer/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    List<TrainerTrainingsResponse> getTrainerTrainings(
        @Parameter(
            description = "Trainer username",
            example = "Syrio.Forel", required = true
        )
        @PathVariable String username,
        @Parameter(
            description = "Filter from date",
            example = "2026-01-01"
        )
        @RequestParam(required = false) LocalDate from,
        @Parameter(
            description = "Filter to date",
            example = "2027-01-01"
        )
        @RequestParam(required = false) LocalDate to,
        @Parameter(
            description = "Filter by trainee username",
            example = "Arya.Stark"
        )
        @RequestParam(required = false) String traineeUsername
    );

    @Operation(
        summary = "Get training types",
        description = "Retrieve all available training types."
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of training types retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = TrainingTypeResponse.class)
            ))
    )
    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    List<TrainingTypeResponse> getTrainingTypes();
}
