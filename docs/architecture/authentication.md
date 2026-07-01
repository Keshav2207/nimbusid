# Authentication Architecture

## Overview

NimbusID implements a stateless authentication model using short-lived JWT access tokens and stateful opaque refresh tokens.

The authentication flow is designed around clear separation of responsibilities:

- JWTs authenticate API requests.
- Refresh tokens manage authenticated sessions.
- User management remains independent from authentication.
- Security infrastructure remains independent from business logic.

---

# Authentication Flow

## Login

```text
Client
    │
    ▼
POST /api/v1/auth/login
    │
    ▼
Authenticate Credentials
    │
    ├───────────────┐
    ▼               ▼
Access JWT      Refresh Token
                    │
               SHA-256 Hash
                    │
             Persist Refresh Token
                    │
                    ▼
Return Access Token + Refresh Token
```

---

## Access Token Authentication

```text
Request
    │
Authorization: Bearer <JWT>
    │
    ▼
JwtAuthenticationFilter
    │
Extract User ID
    │
Load User
    │
Populate SecurityContext
    │
Controller
```

JWT validation is performed by JJWT while parsing the token.

The following validations are automatic:

- Signature verification
- Token expiration (`exp`)
- Token format

---

## Refresh Flow

```text
POST /api/v1/auth/refresh
        │
Receive Refresh Token
        │
Hash Token
        │
Lookup Database
        │
Exists?
Expired?
Revoked?
        │
Generate New Access Token
Generate New Refresh Token
        │
Revoke Previous Refresh Token
        │
Return New Token Pair
```

NimbusID implements **refresh token rotation**.

Every successful refresh invalidates the previous refresh token.

This prevents replay attacks using previously exchanged refresh tokens.

---

## Logout

```text
POST /api/v1/auth/logout
        │
Validate Refresh Token
        │
Revoke Refresh Token
        │
Logout Complete
```

---

# Token Strategy

## Access Token

Format:

- JWT

Purpose:

- Authenticate API requests

Validated using:

- Signature
- Expiration

Storage:

- Client

Persistence:

- Not stored

---

## Refresh Token

Format:

- Opaque random token

Purpose:

- Session continuation

Validated using:

- Database lookup

Persistence:

- SHA-256 hash only

Plaintext refresh tokens are never stored.

---

# Refresh Token Lifecycle

```
Generated
     │
     ▼
Persisted
     │
     ▼
Validated
     │
     ▼
Rotated
     │
     ▼
Revoked
```

---

# Architectural Decisions

## JWT subject stores User UUID

Instead of usernames or emails, NimbusID stores the user's UUID in the JWT subject (`sub`) claim.

Benefits:

- Immutable identifier
- Independent of username/email changes
- Simplifies lookup

---

## Opaque Refresh Tokens

Refresh tokens are intentionally **not JWTs**.

Reasons:

- Simpler validation
- Database-controlled lifecycle
- Easy revocation
- Token rotation support
- No unnecessary claims

---

## SHA-256 Storage

Only the SHA-256 hash of the refresh token is persisted.

Benefits:

- Plaintext tokens are never recoverable
- Similar security model to password hashing
- Database compromise does not expose usable refresh tokens

---

## Stateless Authentication

NimbusID does not maintain HTTP sessions.

Every protected request is authenticated independently using the supplied JWT.

No background process or watcher is required for token expiration.

Expiration is evaluated only when a token is presented.

---

## Session Management

Refresh tokens represent authenticated sessions.

A single user may have multiple active sessions simultaneously.

Example:

- Laptop
- Mobile
- Tablet

Refreshing one session does not affect the others.

---

## SecurityUserService

NimbusID intentionally does not use Spring Security's `UserDetailsService`.

Reason:

Authentication is based on the UUID stored inside the JWT subject rather than a username.

Instead, NimbusID provides:

```
SecurityUserService
```

```
User loadUserById(UUID userId)
```

This aligns the security model with the domain model.

---

## Clock

All time-sensitive operations use the shared `Clock` bean.

Benefits:

- Deterministic testing
- Centralized time source
- Avoids direct use of `Instant.now()`

---

## UUID Generation

NimbusID generates UUIDs within the application rather than relying on database-generated identifiers.

Benefits:

- Database independence
- IDs available before persistence
- Consistent behavior across storage providers

---

## Design Principles Applied

- Domain-driven module boundaries
- YAGNI (avoid speculative abstractions)
- Single Responsibility Principle
- Stateless authentication
- Explicit dependencies
- Infrastructure separated from business logic
- Persistence hidden behind repositories
- Security infrastructure isolated from authentication logic

---

# Current Authentication Capabilities

- User registration
- Password hashing (BCrypt)
- JWT access tokens
- JWT authentication
- Persistent refresh tokens
- Refresh token validation
- Refresh token rotation
- Refresh token revocation
- Logout
- Swagger Bearer authentication

---

# Future Enhancements

- HttpOnly Secure Cookie support
- Refresh token replay detection
- Session listing
- Logout from all devices
- Device metadata
- Roles and permissions
- OAuth 2.1
- OpenID Connect
- SAML
