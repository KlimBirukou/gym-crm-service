package com.epam.gym.controller.advice;

import com.epam.gym.exception.AuthException;
import com.epam.gym.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.exception.conflict.date.DateConflictException;
import com.epam.gym.exception.not.active.NotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.exception.not.found.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final LocalDate DATE = LocalDate.of(2026, 3, 1);
    private static final String ENTITY_NAME = "entity";
    private static final String IDENTIFIER = "identifier";
    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String ERROR_MESSAGE_500 = "An unexpected error occurred on the server side.";
    private static final String VALIDATION_ERROR = "Validation error";

    @Mock
    private WebRequest request;
    @Mock
    private NotFoundException notFoundException;
    @Mock
    private NotActiveException notActiveException;
    @Mock
    private DateConflictException dateConflictException;
    @Mock
    private AlreadyAssignedException alreadyAssignedException;
    @Mock
    private NotAssignmentException notAssignmentException;
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
            notFoundException,
            notActiveException,
            dateConflictException,
            alreadyAssignedException,
            notAssignmentException,
            methodArgumentNotValidException,
            bindingResult,
            httpMediaTypeNotSupportedException
        );
    }

    @Test
    void handleNotFoundException() {
        doReturn(ENTITY_NAME).when(notFoundException).getEntityName();
        doReturn(IDENTIFIER).when(notFoundException).getIdentifier();

        var response = testObject.handleException(notFoundException, request);

        assertResponse(response, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name());
    }

    @Test
    void handleNotActiveException() {
        doReturn(ENTITY_NAME).when(notActiveException).getEntityName();
        doReturn(IDENTIFIER).when(notActiveException).getIdentifier();

        var response = testObject.handleException(notActiveException, request);

        HttpStatus unprocessableContent = HttpStatus.UNPROCESSABLE_CONTENT;
        assertResponse(response, unprocessableContent, unprocessableContent.name());
    }

    @Test
    void handleAlreadyAssignedException() {
        doReturn(TRAINEE_USERNAME).when(alreadyAssignedException).getTraineeUsername();
        doReturn(TRAINER_USERNAME).when(alreadyAssignedException).getTrainerUsername();

        var response = testObject.handleException(alreadyAssignedException, request);

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
    }

    @Test
    void handleNotAssignmentException() {
        doReturn(TRAINEE_USERNAME).when(notAssignmentException).getTraineeUsername();
        doReturn(TRAINER_USERNAME).when(notAssignmentException).getTrainerUsername();

        var response = testObject.handleException(notAssignmentException, request);

        assertResponse(response, HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_CONTENT");
    }

    @Test
    void handleAuthException() {
        var authException = new AuthException();

        var response = testObject.handleException(authException, request);

        assertResponse(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }

    @Test
    void handleDateConflictException() {
        doReturn(ENTITY_NAME).when(dateConflictException).getEntityName();
        doReturn(IDENTIFIER).when(dateConflictException).getIdentifier();
        doReturn(DATE).when(dateConflictException).getDate();

        var response = testObject.handleException(dateConflictException, request);

        assertResponse(response, HttpStatus.CONFLICT, "CONFLICT");
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
        ErrorDto dto = (ErrorDto) response.getBody();
        assertNotNull(dto);
        assertNotNull(dto.description());
        assertEquals("UNSUPPORTED_MEDIA_TYPE", dto.error());
    }

    @Test
    void handleGeneralException() {
        var generalException = new RuntimeException("Unexpected boom");
        var response = testObject.handleGeneralException(generalException, request);

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
