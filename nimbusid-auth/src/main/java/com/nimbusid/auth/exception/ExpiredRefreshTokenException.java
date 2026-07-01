package com.nimbusid.auth.exception;

public class ExpiredRefreshTokenException extends RuntimeException {

    public ExpiredRefreshTokenException() {
        super("Refresh token has expired.");
    }

}
