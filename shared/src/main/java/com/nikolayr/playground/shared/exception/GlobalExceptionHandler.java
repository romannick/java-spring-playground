package com.nikolayr.playground.shared.exception;

import com.nikolayr.playground.shared.dto.response.ValidationErrorResponse;
import com.nikolayr.playground.shared.types.ErrorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import com.nikolayr.playground.shared.dto.response.GenericErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<GenericErrorResponse>> handleEntityNotFound(EntityNotFoundException ex) {
        log.info("EntityNotFoundException exception occurred: {}", ex.getMessage(), ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new GenericErrorResponse(ErrorStatus.BAD_REQUEST_ERROR,ex.getMessage())));
    }

    @ExceptionHandler(InvalidBodyAtLeastOneFieldException.class)
    public Mono<ResponseEntity<GenericErrorResponse>> handleInvalidBodyAtLeastOneFieldException(InvalidBodyAtLeastOneFieldException ex) {
        log.info("InvalidBodyAtLeastOneFieldException occurred: {}", ex.getMessage(), ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new GenericErrorResponse(ErrorStatus.VALIDATION_ERROR, ex.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<GenericErrorResponse>> handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException occurred: {}", ex.getMessage(), ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new GenericErrorResponse(ErrorStatus.BAD_REQUEST_ERROR, ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Mono<ResponseEntity<GenericErrorResponse>> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("UnauthorizedException occurred: {}", ex.getMessage(), ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GenericErrorResponse(ErrorStatus.UNAUTHORIZED_ERROR, ex.getMessage())));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ValidationErrorResponse>> handleValidationException(WebExchangeBindException ex) {
        log.error("WebExchangeBindException occurred: {}", ex.getMessage(), ex);

        Map<String, String> fieldErrors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing // handle duplicate keys
                ));

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                ErrorStatus.VALIDATION_ERROR,
                fieldErrors
        );

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<GenericErrorResponse>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericErrorResponse(ErrorStatus.INTERNAL_SERVER_ERROR,"Internal server error")));
    }
}