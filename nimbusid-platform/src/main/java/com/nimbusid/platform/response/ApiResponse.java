package com.nimbusid.platform.response;

/**
 * Standard API response wrapper returned by all REST endpoints.
 *
 * @param <T> the response payload type
 */
public class ApiResponse<T> {

    private T data;

    private ApiMetadata meta;

    public ApiResponse() {
    }

    public ApiResponse(T data, ApiMetadata meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiMetadata getMeta() {
        return meta;
    }

    public void setMeta(ApiMetadata meta) {
        this.meta = meta;
    }
}
