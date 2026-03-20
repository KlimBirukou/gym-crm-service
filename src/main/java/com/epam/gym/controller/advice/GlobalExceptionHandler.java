package com.epam.gym.controller.advice;

import com.epam.gym.exception.auth.InvalidCredentialsException;
import com.epam.gym.exception.auth.NotAuthenticatedException;
import com.epam.gym.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.exception.conflict.date.DateConflictException;
import com.epam.gym.exception.not.active.NotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.exception.not.found.NotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MESSAGE_500 = "An unexpected error occurred on the server side.";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "%s [%s] was not found";
    private static final String NOT_ASSIGNED_MESSAGE = "Trainer [%s] not assigned to [%s] trainee";
    private static final String ALREADY_ASSIGNED_MESSAGE = "Trainee [%s] already assigned to trainer [%s]";
    private static final String NOT_ACTIVE_MESSAGE = "%s [%s] not active to perform this action";
    private static final String DATE_CONFLICT_MESSAGE = "%s [%s] already has a training on [%s] date";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Username or password invalid";
    private static final String NOT_AUTHENTICATED_MESSAGE =
        "Authentication required. Please login to access this resource";
    private static final String VALIDATION_FAILED = "VALIDATION_FAILED";
    private static final String CONTENT_TYPE_MESSAGE =
        "Content type '%s' is not supported. Supported media types are: %s";

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotFoundException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            ENTITY_NOT_FOUND_MESSAGE.formatted(exception.getEntityName(), exception.getIdentifier()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotActiveException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            NOT_ACTIVE_MESSAGE.formatted(exception.getEntityName(), exception.getIdentifier()),
            HttpStatus.UNPROCESSABLE_CONTENT
        );
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(DateConflictException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            DATE_CONFLICT_MESSAGE.formatted(exception.getEntityName(), exception.getIdentifier(), exception.getDate()),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(AlreadyAssignedException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            ALREADY_ASSIGNED_MESSAGE.formatted(exception.getTraineeUsername(), exception.getTrainerUsername()),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotAssignmentException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            NOT_ASSIGNED_MESSAGE.formatted(exception.getTraineeUsername(), exception.getTrainerUsername()),
            HttpStatus.UNPROCESSABLE_CONTENT
        );
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(InvalidCredentialsException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            INVALID_CREDENTIALS_MESSAGE,
            HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotAuthenticatedException exception, WebRequest request) {
        return getObjectResponseEntity(
            exception,
            request,
            NOT_AUTHENTICATED_MESSAGE,
            HttpStatus.UNAUTHORIZED
        );
    }

    @Override
    protected @Nullable ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                                     @NonNull HttpHeaders headers,
                                                                                     @NonNull HttpStatusCode status,
                                                                                     @NonNull WebRequest request) {
        var message = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("; "));
        return getObjectResponseEntity(
            ex,
            request,
            message,
            VALIDATION_FAILED,
            HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected @Nullable ResponseEntity<@NonNull Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                                        HttpHeaders headers,
                                                                                        HttpStatusCode status,
                                                                                        WebRequest request) {
        var supportedTypes = ex.getSupportedMediaTypes()
            .stream()
            .map(MediaType::toString)
            .collect(Collectors.joining("; "));
        return getObjectResponseEntity(
            ex,
            request,
            CONTENT_TYPE_MESSAGE.formatted(ex.getContentType(), supportedTypes),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull Object> handleGeneralException(Exception exception, WebRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error", exception);
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(MESSAGE_500)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    private ResponseEntity<@NonNull Object> getObjectResponseEntity(Exception exception,
                                                                    WebRequest request,
                                                                    String message,
                                                                    HttpStatus status) {
        return getObjectResponseEntity(exception, request, message, status.name(), status);
    }

    private ResponseEntity<@NonNull Object> getObjectResponseEntity(Exception exception,
                                                                    WebRequest request,
                                                                    String message,
                                                                    String error,
                                                                    HttpStatus status) {
        log.info(message);
        var errorDto = ErrorDto.builder()
            .error(error)
            .description(message)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }
}
