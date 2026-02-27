package com.epam.gym.controller.advice;

import com.epam.gym.exception.conflict.DateConflictException;
import com.epam.gym.exception.not.active.NotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.exception.not.found.NotFoundException;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotFoundException exception, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(exception.getMessage())
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(DateConflictException exception, WebRequest request) {
        var status = HttpStatus.CONFLICT;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description(exception.getMessage())
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotAssignmentException exception, WebRequest request) {
        var errorDto = ErrorDto.builder()
            .error("NOT_ASSIGNMENT")
            .description(exception.getMessage())
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), HttpStatus.UNPROCESSABLE_CONTENT,
            request);
    }

    @ExceptionHandler
    public ResponseEntity<@NonNull Object> handleException(NotActiveException exception, WebRequest request) {
        var errorDto = ErrorDto.builder()
            .error("USER_NOT_ACTIVE")
            .description(exception.getMessage())
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), HttpStatus.UNPROCESSABLE_CONTENT,
            request);
    }

    @Override
    protected @Nullable ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                                     HttpHeaders headers,
                                                                                     HttpStatusCode status,
                                                                                     WebRequest request) {
        var errorDto = ErrorDto.builder()
            .error("VALIDATION_FAILED")
            .description(ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; ")))
            .build();
        return super.handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST,
            request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull Object> handleGeneralException(Exception exception, WebRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var errorDto = ErrorDto.builder()
            .error(status.name())
            .description("An unexpected error occurred on the server side.")
            .build();
        return super.handleExceptionInternal(exception, errorDto, new HttpHeaders(), status, request);
    }
}
