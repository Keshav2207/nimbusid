package com.nimbusid.platform.response;

import java.time.Instant;

public class ApiMetadata {
    private Instant timestamp;
    private String path;
    private String requestId;

    public ApiMetadata() {
    }

    public ApiMetadata(String requestId, String path, Instant timestamp) {
        this.requestId = requestId;
        this.path = path;
        this.timestamp = timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
