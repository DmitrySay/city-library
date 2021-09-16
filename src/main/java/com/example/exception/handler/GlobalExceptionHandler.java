package com.example.exception.handler;

import com.example.exception.error.ErrorResponseDto;
import com.example.exception.error.FieldValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    ResponseEntity<?> handleException(Throwable ex) {
        log.error("Caught unhandled exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handle(AccessDeniedException ex) {
        log.error("Caught AccessDeniedException: {}", ex.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

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
}
