package com.epam.gym.controller.advice;

import com.epam.gym.exception.auth.AccountTemporarilyBlockedException;
import com.epam.gym.exception.auth.InvalidCredentialsException;
import com.epam.gym.exception.auth.NotAuthenticatedException;
import com.epam.gym.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.exception.conflict.date.TraineeDateConflictException;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.exception.not.found.TraineeNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final LocalDate DATE = LocalDate.of(2026, 3, 1);
    private static final String IDENTIFIER = "identifier";
    private static final String TRAINEE_USERNAME = "trainee username";
    private static final String TRAINER_USERNAME = "trainer username";
    private static final String VALIDATION_ERROR = "validation error";
    private static final long MINUTES_REMAINING = 10L;
    private static final String ERROR_MESSAGE_500 = "An unexpected error occurred on the server side.";

    @Mock
    private WebRequest request;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private ObjectError objectError;
    @Mock
    private HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException;

    @InjectMocks
    private GlobalExceptionHandler testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
            request,
            methodArgumentNotValidException,
            bindingResult,
            objectError,
            httpMediaTypeNotSupportedException
        );
    }

    @Test
    void handleInvalidCredentialsException() {
        var response = testObject.handleUnauthorizedException(new InvalidCredentialsException(), request);

        assertResponse(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        assertDescriptionContains(response, "password");
    }

    @Test
    void handleNotAuthenticatedException() {
        var response = testObject.handleUnauthorizedException(new NotAuthenticatedException(), request);

        assertResponse(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        assertDescriptionContains(response, "Authentication required");
    }

    @Test
    void handleAccountTemporarilyBlockedException() {
        var response = testObject.handleException(
            new AccountTemporarilyBlockedException(MINUTES_REMAINING),
            request
        );

        assertResponse(response, HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_REQUESTS");
        assertDescriptionContains(response, String.valueOf(MINUTES_REMAINING));
    }

    @Test
    void handleNotFoundException() {
        var response = testObject.handleException(new TraineeNotFoundException(IDENTIFIER), request);

        assertResponse(response, HttpStatus.NOT_FOUND, "NOT_FOUND");
        assertDescriptionContains(response, IDENTIFIER);
    }

    @ParameterizedTest
    @MethodSource("provideNullIdentifiers")
    void handleNotFoundException_nullIdentifier(String identifier) {
        var response = testObject.handleException(new TraineeNotFoundException(identifier), request);

        assertResponse(response, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

    @Test
    void handleNotActiveException() {
        var response = testObject.handleException(new TraineeNotActiveException(IDENTIFIER), request);

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
        assertDescriptionContains(response, IDENTIFIER);
    }

    @ParameterizedTest
    @MethodSource("provideNullIdentifiers")
    void handleNotActiveException_nullIdentifier(String identifier) {
        var response = testObject.handleException(new TraineeNotActiveException(identifier), request);

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
    }

    @Test
    void handleDateConflictException() {
        var response = testObject.handleException(
            new TraineeDateConflictException(IDENTIFIER, DATE),
            request
        );

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
        assertDescriptionContains(response, IDENTIFIER);
        assertDescriptionContains(response, DATE.toString());
    }

    @ParameterizedTest
    @MethodSource("provideNullDatesAndIdentifiers")
    void handleDateConflictException_nullFields(String identifier, LocalDate date) {
        var response = testObject.handleException(
            new TraineeDateConflictException(identifier, date),
            request
        );

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
    }

    @Test
    void handleAlreadyAssignedException() {
        var response = testObject.handleException(
            new AlreadyAssignedException(TRAINEE_USERNAME, TRAINER_USERNAME),
            request
        );

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
        assertDescriptionContains(response, TRAINEE_USERNAME);
        assertDescriptionContains(response, TRAINER_USERNAME);
    }

    @ParameterizedTest
    @MethodSource("provideNullUsernames")
    void handleAlreadyAssignedException_nullFields(String traineeUsername, String trainerUsername) {
        var response = testObject.handleException(
            new AlreadyAssignedException(traineeUsername, trainerUsername),
            request
        );

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
    }

    @Test
    void handleNotAssignmentException() {
        var response = testObject.handleException(
            new NotAssignmentException(TRAINEE_USERNAME, TRAINER_USERNAME),
            request
        );

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
        assertDescriptionContains(response, TRAINEE_USERNAME);
        assertDescriptionContains(response, TRAINER_USERNAME);
    }

    @ParameterizedTest
    @MethodSource("provideNullUsernames")
    void handleNotAssignmentException_nullFields(String traineeUsername, String trainerUsername) {
        var response = testObject.handleException(
            new NotAssignmentException(traineeUsername, trainerUsername),
            request
        );

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
    }

    @Test
    void handleMethodArgumentNotValid() {
        doReturn(VALIDATION_ERROR).when(objectError).getDefaultMessage();
        doReturn(List.of(objectError)).when(bindingResult).getAllErrors();
        doReturn(bindingResult).when(methodArgumentNotValidException).getBindingResult();

        var response = testObject.handleMethodArgumentNotValid(
            methodArgumentNotValidException,
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );

        assertResponse(response, HttpStatus.BAD_REQUEST, "VALIDATION_FAILED");
        assertNotNull(response);
        assertDescriptionContains(response, VALIDATION_ERROR);
    }

    @Test
    void handleHttpMediaTypeNotSupported() {
        doReturn(MediaType.TEXT_PLAIN).when(httpMediaTypeNotSupportedException).getContentType();
        doReturn(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML))
            .when(httpMediaTypeNotSupportedException).getSupportedMediaTypes();

        var response = testObject.handleHttpMediaTypeNotSupported(
            httpMediaTypeNotSupportedException,
            new HttpHeaders(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            request
        );

        assertResponse(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE");
        assertNotNull(response);
        assertDescriptionContains(response, MediaType.TEXT_PLAIN.toString());
    }

    @Test
    void handleGeneralException() {
        var response = testObject.handleGeneralException(new RuntimeException("boom"), request);

        assertResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
        assertDescriptionContains(response, ERROR_MESSAGE_500);
    }

    static Stream<Arguments> provideNullIdentifiers() {
        return Stream.of(
            Arguments.of((String) null)
        );
    }

    static Stream<Arguments> provideNullUsernames() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME),
            Arguments.of(TRAINEE_USERNAME, null),
            Arguments.of(null, null)
        );
    }

    static Stream<Arguments> provideNullDatesAndIdentifiers() {
        return Stream.of(
            Arguments.of(null, DATE),
            Arguments.of(IDENTIFIER, null),
            Arguments.of(null, null)
        );
    }

    private void assertResponse(ResponseEntity<?> response, HttpStatus expectedStatus, String expectedError) {
        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatusCode());
        assertInstanceOf(ErrorDto.class, response.getBody());
        assertEquals(expectedError, ((ErrorDto) response.getBody()).error());
    }

    private void assertDescriptionContains(ResponseEntity<?> response, String fragment) {
        var dto = (ErrorDto) response.getBody();
        assertNotNull(dto);
        assertThat(dto.description()).contains(fragment);
    }
}
