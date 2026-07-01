package com.nimbusid.auth.refresh;

/**
 * Generates cryptographically secure refresh tokens.
 */
public interface RefreshTokenGenerator {
    String generate();
}
