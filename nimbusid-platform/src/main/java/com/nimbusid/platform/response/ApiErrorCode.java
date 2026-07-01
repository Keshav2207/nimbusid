package com.nimbusid.platform.response;

public enum ApiErrorCode {

    INVALID_CREDENTIAL("INVALID_CREDENTIAL"),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED"),

    VALIDATION_ERROR("VALIDATION_ERROR"),

    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN"),
    EXPIRED_REFRESH_TOKEN("EXPIRED_REFRESH_TOKEN"),
    REVOKED_REFRESH_TOKEN("REVOKED_REFRESH_TOKEN"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    private final String code;

    ApiErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
