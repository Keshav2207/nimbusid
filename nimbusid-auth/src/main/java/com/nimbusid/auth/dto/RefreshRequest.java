package com.nimbusid.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for refreshing an access token.
 */
public record RefreshRequest(

        @NotBlank
        String refreshToken

) {
}