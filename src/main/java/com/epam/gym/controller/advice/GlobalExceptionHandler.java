package com.epam.gym.controller.advice;

import com.epam.gym.exception.AuthException;
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
import org.springframework.http.ResponseEntity;
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
    private static final String LOGIN_MESSAGE = "Username or password invalid";

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotFoundException exception, WebRequest request) {
        var message = ENTITY_NOT_FOUND_MESSAGE.formatted(exception.getEntityName(), exception.getIdentifier());
        log.warn(message);
        var status = HttpStatus.NOT_FOUND;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(message)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotActiveException exception, WebRequest request) {
        var message = NOT_ACTIVE_MESSAGE.formatted(exception.getEntityName(), exception.getIdentifier());
        log.warn(message);
        var status = HttpStatus.NOT_FOUND;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(message)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(DateConflictException exception, WebRequest request) {
        var message = DATE_CONFLICT_MESSAGE.formatted(exception.getEntityName(), exception.getIdentifier(),
            exception.getDate());
        log.warn(message);
        var status = HttpStatus.CONFLICT;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(message)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(AlreadyAssignedException exception, WebRequest request) {
        var message = ALREADY_ASSIGNED_MESSAGE.formatted(exception.getTraineeUsername(),
            exception.getTrainerUsername());
        log.warn(message);
        var status = HttpStatus.CONFLICT;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(message)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotAssignmentException exception, WebRequest request) {
        var message = NOT_ASSIGNED_MESSAGE.formatted(exception.getTraineeUsername(), exception.getTrainerUsername());
        log.warn(message);
        var status = HttpStatus.UNPROCESSABLE_CONTENT;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(message)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(AuthException exception, WebRequest request) {
        log.warn(LOGIN_MESSAGE);
        var status = HttpStatus.UNAUTHORIZED;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(LOGIN_MESSAGE)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
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
        log.warn(message);
        var errorDto = ErrorDto.builder()
            .error("VALIDATION_FAILED")
            .description(message)
            .build();
        return super.handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST,
            request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull Object> handleGeneralException(Exception exception, WebRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(MESSAGE_500)
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }
}
