package com.nimbusid.auth.service;

import com.nimbusid.auth.entity.RefreshToken;
import com.nimbusid.auth.exception.ExpiredRefreshTokenException;
import com.nimbusid.auth.exception.InvalidRefreshTokenException;
import com.nimbusid.auth.exception.RevokedRefreshTokenException;
import com.nimbusid.auth.refresh.RefreshTokenGenerator;
import com.nimbusid.auth.refresh.RefreshTokenHasher;
import com.nimbusid.auth.repository.RefreshTokenRepository;
import com.nimbusid.auth.token.JwtProperties;
import com.nimbusid.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * Manages the lifecycle of refresh tokens.
 */
@Service
public class NimbusRefreshTokenService implements RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenHasher tokenHasher;
    private final JwtProperties properties;
    private final Clock clock;

    public NimbusRefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenGenerator refreshTokenGenerator,
            RefreshTokenHasher tokenHasher,
            JwtProperties properties,
            Clock clock) {

        this.repository = refreshTokenRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.tokenHasher = tokenHasher;
        this.properties = properties;
        this.clock = clock;
    }

    /**
     * Creates and persists a new refresh token for the supplied user.
     *
     * @param user the authenticated user
     * @return the plaintext refresh token
     */
    public String generateRefreshToken(User user) {

        String refreshToken = refreshTokenGenerator.generate();
        Instant now = Instant.now(clock);

        RefreshToken entity = RefreshToken.create(
                UUID.randomUUID(),
                user,
                tokenHasher.hash(refreshToken),
                now,
                now.plus(properties.getRefreshTokenValidity())
        );

        // Hashed token being saved to database.
        repository.save(entity);

        // Original token is sent back.
        return refreshToken;
    }

    public RefreshToken validateRefreshToken(String refreshToken) {

        String tokenHash = tokenHasher.hash(refreshToken);
        RefreshToken token = repository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);
        Instant now = Instant.now(clock);

        if (token.getRevokedAt() != null) {
            throw new RevokedRefreshTokenException();
        }

        // understand why token.getExpiresAt().isBefore(now) is not used here..
        if (!token.getExpiresAt().isAfter(now)) {
            throw new ExpiredRefreshTokenException();
        }

        return token;
    }

    /**
     * Revokes current refresh token and generates new one.
     */
    public String rotateRefreshToken(RefreshToken refreshToken) {
        // Revocation
        revokeRefreshToken(refreshToken);

        // Issue new token.
        return generateRefreshToken(refreshToken.getUser());
    }

    /**
     * Revokes current refresh token (for logout purposes).
     */
    public void revokeRefreshToken(RefreshToken refreshToken) {
        Instant now = Instant.now(clock);
        refreshToken.setLastUsedAt(now);
        refreshToken.setRevokedAt(now);
        repository.save(refreshToken);
    }

}
