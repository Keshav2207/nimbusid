package com.nimbusid.platform.factory;


import com.nimbusid.platform.response.*;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

/**
 * Factory responsible for creating standardized API responses.
 *
 * <p>This class centralizes the creation of response objects so that all REST
 * endpoints return a consistent response structure throughout the application.</p>
 *
 * <p>This class is transport agnostic and should not have any dependency on
 * Spring MVC or ResponseEntity.</p>
 */
public class ResponseFactory {

    private final Clock clock;

    public ResponseFactory(Clock clock) {
        this.clock = clock;
    }

    /**
     * Creates a successful API response.
     *
     * @param data response payload
     * @param <T> payload type
     * @return standardized success response
     */
    public <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, createMetadata());
    }

    /**
     * Creates a successful API response for newly created resources.
     *
     * @param data response payload
     * @param <T> payload type
     * @return standardized creation response
     */
    public <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(data, createMetadata());
    }

    /**
     * Creates a standardized error response.
     *
     * @param error error details
     * @return standardized error response
     */
    public ApiErrorResponse error(ApiError error) {
        return new ApiErrorResponse(error, createMetadata());
    }

    // Method overloading.
    public ApiErrorResponse error(ApiErrorCode errorCode, String message) {
        ApiError error = new ApiError();
        error.setCode(errorCode.getCode());
        error.setMessage(message);
        return error(error);
    }

    public ApiErrorResponse error(ApiErrorCode errorCode, String message, List<ValidationError> validationErrors) {
        ApiError error = new ApiError();
        error.setCode(errorCode.getCode());
        error.setMessage(message);
        error.setValidationErrors(validationErrors);
        return error(error);
    }

    /**
     * Creates response metadata.
     *
     * @return metadata populated with default values
     */
    private ApiMetadata createMetadata() {

        ApiMetadata metadata = new ApiMetadata();

        metadata.setTimestamp(Instant.now(clock));
        metadata.setPath(null);
        metadata.setRequestId(null);

        return metadata;
    }
}
