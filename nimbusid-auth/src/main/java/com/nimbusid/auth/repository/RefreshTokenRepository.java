package com.nimbusid.auth.repository;

import com.nimbusid.auth.entity.RefreshToken;
import com.nimbusid.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing persistent refresh tokens.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Finds a refresh token by its SHA-256 hash.
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * Finds all refresh tokens issued to the given user.
     */
    List<RefreshToken> findByUser(User user);

}
