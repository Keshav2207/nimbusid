package com.nimbusid.auth.exception;

public class RevokedRefreshTokenException extends RuntimeException {

    public RevokedRefreshTokenException() {
        super("Refresh token has been revoked.");
    }

}
