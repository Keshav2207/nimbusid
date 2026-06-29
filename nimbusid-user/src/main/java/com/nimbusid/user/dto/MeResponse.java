package com.nimbusid.user.dto;

import java.util.UUID;

public record MeResponse(
        UUID id,
        String email,
        boolean enabled
) {
}