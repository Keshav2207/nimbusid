package com.nimbusid.exception;

import com.nimbusid.auth.exception.*;
import com.nimbusid.platform.factory.ResponseFactory;
import com.nimbusid.platform.response.ApiErrorCode;
import com.nimbusid.platform.response.ApiErrorResponse;
import com.nimbusid.platform.response.ValidationError;
import com.nimbusid.user.exception.EmailAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;


@RestControllerAdvice

public class GlobalExceptionHandler {

    private final ResponseFactory responseFactory;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseFactory.error(
                        ApiErrorCode.EMAIL_ALREADY_EXISTS,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredential(
            InvalidCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseFactory.error(
                        ApiErrorCode.INVALID_CREDENTIAL,
                        ex.getMessage()));
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountDisabled(
            AccountDisabledException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(responseFactory.error(
                        ApiErrorCode.ACCOUNT_DISABLED,
                        ex.getMessage()));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRefreshToken(
            InvalidRefreshTokenException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseFactory.error(
                        ApiErrorCode.INVALID_REFRESH_TOKEN,
                        ex.getMessage()));
    }

    @ExceptionHandler(RevokedRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleRevokedRefreshToken(
            RevokedRefreshTokenException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseFactory.error(
                        ApiErrorCode.REVOKED_REFRESH_TOKEN,
                        ex.getMessage()));
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredRefreshToken(
            ExpiredRefreshTokenException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseFactory.error(
                        ApiErrorCode.EXPIRED_REFRESH_TOKEN,
                        ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(
                        fieldError.getField(),
                        Objects.requireNonNullElse(
                                fieldError.getDefaultMessage(),
                                "Invalid value")))
                .toList();

        return ResponseEntity.badRequest()
                .body(responseFactory.error(
                        ApiErrorCode.VALIDATION_ERROR,
                        "Validation failed.",
                        validationErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {

        LOGGER.error("Unhandled exception while processing request.", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseFactory.error(
                        ApiErrorCode.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred."));
    }
}
