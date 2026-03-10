package com.epam.gym.controller.rest.assignment;

import com.epam.gym.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Tag(
    name = "Assignment",
    description = "Assigning trainer to trainee and obtaining a list of assigned/unassigned, active/inactive trainee and trainers."
)
@RequestMapping("/api/v1/assignments")
public interface IAssignmentController {

    @Operation(
        summary = "Get trainees for trainer",
        description = "Retrieve a filtered list of trainees for a specific trainer. " +
            "Filter by assignment status (assigned/unassigned) and activity status (active/inactive)."
    )
    @ApiResponse(
        responseCode = "204",
        description = "List of trainees successfully retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = TraineeProfileResponse.class)
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
                      "description": "Trainer [Night.King] was not found"
                    }
                    """
            )
        )
    )
    @GetMapping(
        path = "/trainer/{username}/trainees",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    List<TraineeProfileResponse> getTrainees(
        @Parameter(
            description = "Trainer username",
            example = "Vesemir.Oldman",
            required = true
        )
        @PathVariable String username,
        @Parameter(
            description = "Assignment filter: true for assigned trainees, false for unassigned",
            example = "true",
            required = true
        )
        @RequestParam Boolean assigned,
        @Parameter(
            description = "Activity filter: true for active trainees, false for inactive",
            example = "true",
            required = true
        )
        @RequestParam Boolean active
    );

    @Operation(
        summary = "Get Trainee list",
        description = "Retrieve a filtered list of trainers for a specific trainee. " +
            "Filter by assignment status (assigned/unassigned) and activity status (active/inactive)."
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of trainers successfully retrieved",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = TrainerProfileResponse.class)
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
    @GetMapping(
        path = "/trainee/{username}/trainers",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    List<TrainerProfileResponse> getTrainers(
        @Parameter(
            description = "Trainee username",
            example = "Arya.Stark",
            required = true
        )
        @PathVariable String username,
        @Parameter(
            description = "Assignment filter: true for assigned trainers, false for unassigned",
            example = "true",
            required = true
        )
        @RequestParam Boolean assigned,
        @Parameter(
            description = "Activity filter: true for active trainees, false for inactive",
            example = "true",
            required = true
        )
        @RequestParam Boolean active
    );

    @Operation(
        summary = "Assign trainee to trainer",
        description = "Create an assignment between a trainee and a trainer. " +
            "Both trainee and trainer must exist and be active."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Trainer successfully assigned to trainee"
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
        description = "Assignment already exists",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Conflict error",
                value = """
                    {
                      "error": "CONFLICT",
                      "description": "Assignment already exists between trainee [Arya.Stark] and trainer [Jaqen.Hghar]"
                    }
                    """
            )
        )
    )
    @ApiResponse(
        responseCode = "422",
        description = "User is not active",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = {
                @ExampleObject(
                    name = "Not active error",
                    value = """
                        {
                          "error": "UNPROCESSABLE_CONTENT",
                          "description": "Trainer [John.Wick] is not active"
                        }
                        """
                ),
                @ExampleObject(
                    name = "Not active error",
                    value = """
                        {
                          "error": "UNPROCESSABLE_CONTENT",
                          "description": "Trainee [Brienne.Tarth] is not active"
                        }
                        """
                )
            }
        )
    )
    @PostMapping(
        path = "/assign",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    void assign(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Assignment details",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AssignRequest.class)
            )
        )
        @Valid @RequestBody AssignRequest request
    );
}
