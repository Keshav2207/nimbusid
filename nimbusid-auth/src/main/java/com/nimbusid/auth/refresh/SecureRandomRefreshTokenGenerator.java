package com.nimbusid.auth.refresh;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Default implementation of {@link RefreshTokenGenerator}.
 *
 * Generates 256-bit cryptographically secure random refresh tokens
 * encoded using Base64 URL encoding without padding.
 */
@Component
public class SecureRandomRefreshTokenGenerator implements RefreshTokenGenerator {

    private static final int TOKEN_SIZE_BYTES = 32;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generate() {

        byte[] bytes = new byte[TOKEN_SIZE_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

}
