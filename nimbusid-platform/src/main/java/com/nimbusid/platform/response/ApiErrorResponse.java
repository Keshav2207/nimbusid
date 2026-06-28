package com.nimbusid.platform.response;

public class ApiErrorResponse {
    private ApiError error;
    private ApiMetadata meta;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(ApiError error, ApiMetadata meta) {
        this.error = error;
        this.meta = meta;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }

    public ApiMetadata getMeta() {
        return meta;
    }

    public void setMeta(ApiMetadata meta) {
        this.meta = meta;
    }
}
