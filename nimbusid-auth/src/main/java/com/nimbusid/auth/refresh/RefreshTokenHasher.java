package com.nimbusid.auth.refresh;

/**
 * Computes a secure hash of refresh tokens before persistence.
 */
public interface RefreshTokenHasher {
    String hash(String refreshToken);
}
