package com.epam.gym.controller.advice;

import com.epam.gym.exception.AuthException;
import com.epam.gym.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.exception.conflict.date.DateConflictException;
import com.epam.gym.exception.not.active.NotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.exception.not.found.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String ENTITY_NAME = "Entity";
    private static final String IDENTIFIER = "identifier_123";
    private static final String TRAINEE_USERNAME = "Trainee.User";
    private static final String TRAINER_USERNAME = "Trainer.User";
    private static final String ERROR_MESSAGE_500 = "An unexpected error occurred on the server side.";

    private WebRequest request;

    private GlobalExceptionHandler testObject;

    @BeforeEach
    void setUp() {
        testObject = new GlobalExceptionHandler();
        request = mock(WebRequest.class);
    }

    @Test
    void handleNotFoundException() {
        var ex = mock(NotFoundException.class);
        when(ex.getEntityName()).thenReturn(ENTITY_NAME);
        when(ex.getIdentifier()).thenReturn(IDENTIFIER);

        var response = testObject.handleException(ex, request);

        assertResponse(response, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

    @Test
    void handleNotActiveException() {
        var ex = mock(NotActiveException.class);
        when(ex.getEntityName()).thenReturn(ENTITY_NAME);
        when(ex.getIdentifier()).thenReturn(IDENTIFIER);

        var response = testObject.handleException(ex, request);

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
    }

    @Test
    void handleAlreadyAssignedException() {
        var ex = mock(AlreadyAssignedException.class);
        when(ex.getTraineeUsername()).thenReturn(TRAINEE_USERNAME);
        when(ex.getTrainerUsername()).thenReturn(TRAINER_USERNAME);

        var response = testObject.handleException(ex, request);

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
    }

    @Test
    void handleNotAssignmentException() {
        var ex = mock(NotAssignmentException.class);
        when(ex.getTraineeUsername()).thenReturn(TRAINEE_USERNAME);
        when(ex.getTrainerUsername()).thenReturn(TRAINER_USERNAME);

        var response = testObject.handleException(ex, request);

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
    }

    @Test
    void handleAuthException() {
        var ex = new AuthException();

        var response = testObject.handleException(ex, request);

        assertResponse(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }

    @Test
    void handleDateConflictException() {
        var ex = mock(DateConflictException.class);
        when(ex.getEntityName()).thenReturn(ENTITY_NAME);
        when(ex.getIdentifier()).thenReturn(IDENTIFIER);
        when(ex.getDate()).thenReturn(LocalDate.now());

        var response = testObject.handleException(ex, request);

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
    }

    @Test
    void handleMethodArgumentNotValid() {
        var ex = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(BindingResult.class);
        var error = mock(ObjectError.class);

        when(error.getDefaultMessage()).thenReturn("Validation error");
        when(bindingResult.getAllErrors()).thenReturn(List.of(error));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        var response = testObject.handleMethodArgumentNotValid(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

        assertResponse(response, HttpStatus.BAD_REQUEST, "VALIDATION_FAILED");
    }

    @Test
    void handleGeneralException() {
        var ex = new RuntimeException("Unexpected boom");

        var response = testObject.handleGeneralException(ex, request);

        assertResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");

        ErrorDto dto = (ErrorDto) response.getBody();
        assertNotNull(dto);
        assertEquals(ERROR_MESSAGE_500, dto.description());
    }

    private void assertResponse(ResponseEntity<?> response, HttpStatus expectedStatus, String expectedError) {
        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatusCode());
        assertInstanceOf(ErrorDto.class, response.getBody());

        ErrorDto dto = (ErrorDto) response.getBody();
        assertNotNull(dto);
        assertEquals(expectedError, dto.error());
    }
}
