package com.nimbusid.auth.service;

import com.nimbusid.auth.entity.RefreshToken;
import com.nimbusid.user.entity.User;

public interface RefreshTokenService {
    String generateRefreshToken(User user);
    RefreshToken validateRefreshToken(String refreshToken);

    /**
     * Rotates the supplied refresh token.
     *
     * <p>The current refresh token is revoked and a new refresh token is
     * generated, persisted and returned.</p>
     */
    String rotateRefreshToken(RefreshToken refreshToken);

    void revokeRefreshToken(RefreshToken refreshToken);
}
