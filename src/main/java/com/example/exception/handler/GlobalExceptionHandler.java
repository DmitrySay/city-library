package com.example.exception.handler;

import com.example.exception.RestException;
import com.example.exception.error.ErrorResponseDto;
import com.example.exception.error.FieldValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    ResponseEntity<?> handle(Throwable ex) {
        log.error("Caught unhandled exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponseDto handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Caught AccessDeniedException: {}", ex.getMessage());
        return ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now())
                .build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException: {}", ex.getMessage());
        BindingResult result = ex.getBindingResult();
        final FieldValidationError error = FieldValidationError.builder()
                .status(BAD_REQUEST)
                .message("Field validation error")
                .build();
        result.getFieldErrors().forEach(fieldError -> error
                .addFieldError(fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage())
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponseDto handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Caught BadCredentialsException: {}", ex.getMessage());
        return ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now())
                .build();

    }

    @ExceptionHandler(RestException.class)
    public ErrorResponseDto handleRestException(RestException ex) {
        log.error("Caught RestException: {}", ex.getMessage());
        return ErrorResponseDto.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now())
                .build();
    }
}
