package com.epam.gym.crm.controller.rest.auth;

import com.epam.gym.crm.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.crm.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.crm.controller.rest.auth.dto.response.RegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Authentification",
    description = "Operations for registration and authentication"
)
@RequestMapping("/api/v1/auth")
public interface IAuthController {

    @Operation(
        summary = "Register trainee",
        description = "Create a new trainee profile and get credentials."
    )
    @SecurityRequirements
    @ApiResponse(
        responseCode = "201",
        description = "Trainee registered successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = RegistrationResponse.class))
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
                      "description": "First name is required"
                    }
                    """
            )
        )
    )
    @PostMapping(
        path = "/registration/trainee",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    RegistrationResponse registerTrainee(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Trainee registration details",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RegisterTraineeRequest.class))
        )
        @Valid @RequestBody RegisterTraineeRequest request
    );

    @Operation(
        summary = "Register trainer",
        description = "Create a new trainer profile and get credentials."
    )
    @SecurityRequirements
    @ApiResponse(
        responseCode = "201",
        description = "Trainer registered successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = RegistrationResponse.class))
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
                      "description": "First name is required"
                    }
                    """
            )
        )
    )
    @PostMapping(
        path = "/registration/trainer",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    RegistrationResponse registerTrainer(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Trainer registration details",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RegisterTrainerRequest.class))
        )
        @Valid @RequestBody RegisterTrainerRequest request
    );

    @Operation(
        summary = "Login",
        description = "Authenticate user using username and password."
    )
    @SecurityRequirements
    @ApiResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = LoginResponse.class))
    )
    @PostMapping(
        path = "/login",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    LoginResponse login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LoginRequest.class))
        )
        @Valid @RequestBody LoginRequest request
    );

    @Operation(
        summary = "Logout",
        description = "Invalidate the current session. The client must discard the token after this call."
    )
    @ApiResponse(responseCode = "204", description = "Logout successfully")
    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout();

    @Operation(
        summary = "Change password",
        description = "Update user password."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Password changed successfully")
    @ApiResponse(
        responseCode = "401",
        description = "Wrong username or password",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Username or password invalid",
                value = """
                    {
                      "error": "UNAUTHORIZED",
                      "description": "Username or password invalid"
                    }
                    """
            )
        )
    )
    @PutMapping(
        path = "/change-password",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    void changePassword(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Password change details",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ChangePasswordRequest.class))
        )
        @Valid @RequestBody ChangePasswordRequest request
    );
}
