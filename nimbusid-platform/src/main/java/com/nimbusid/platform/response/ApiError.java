package com.nimbusid.platform.response;

import java.util.List;

public class ApiError {
    private String code;
    private String message;
    private List<ValidationError> validationErrors;

    public ApiError() {
    }

    public ApiError(String code, String message, List<ValidationError> validationErrors) {
        this.code = code;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}