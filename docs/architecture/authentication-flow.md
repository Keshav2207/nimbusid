# Authentication Flow

## Introduction

NimbusID uses stateless authentication based on JSON Web Tokens (JWT).

A user authenticates once using their credentials. Upon successful authentication, the server issues an Access Token and a Refresh Token.

Subsequent requests are authenticated by validating the Access Token contained in the `Authorization` header.

No HTTP session is maintained on the server.

---

# Authentication Components

The authentication pipeline is distributed across three modules.

| Module            | Responsibility                                 |
| ----------------- | ---------------------------------------------- |
| nimbusid-user     | User persistence and lookup                    |
| nimbusid-auth     | Login and JWT generation                       |
| nimbusid-security | JWT validation and Spring Security integration |

Each module owns a distinct responsibility and avoids overlapping concerns.

---

# Login Flow

The login endpoint authenticates a user and issues JWTs.

```text
Client
    │
    ▼
POST /api/v1/auth/login
    │
    ▼
AuthController
    │
    ▼
AuthService
    │
    ▼
UserService
    │
Find User by Email
    │
    ▼
PasswordEncoder.matches(...)
    │
    ▼
Generate Access Token
Generate Refresh Token
    │
    ▼
Return Token Pair
```

At this stage, the Refresh Token is generated but not yet persisted. A future milestone introduces persistent refresh tokens with rotation.

---

# Access Token

The Access Token is a signed JWT.

Current claims:

| Claim | Description     |
| ----- | --------------- |
| iss   | Token issuer    |
| sub   | User UUID       |
| iat   | Issued at       |
| exp   | Expiration time |

NimbusID intentionally keeps the JWT payload minimal.

Future claims may include:

* Roles
* Permissions
* Tenant
* Organization
* OAuth scopes

---

# Protected Request Flow

Every protected request follows the same authentication pipeline.

```text
Client
    │
Authorization: Bearer <JWT>
    │
    ▼
JwtAuthenticationFilter
    │
Extract JWT
    │
Validate Signature
    │
Check Expiration
    │
Extract UUID
    │
    ▼
SecurityUserService
    │
Load User
    │
    ▼
Create Authentication
    │
    ▼
SecurityContextHolder
    │
    ▼
Controller
```

Only after the `SecurityContext` is populated does Spring Security consider the request authenticated.

---

# JWT Authentication Filter

`JwtAuthenticationFilter` is responsible for authenticating incoming requests.

Its responsibilities include:

1. Read the `Authorization` header.
2. Extract the Bearer token.
3. Validate the JWT.
4. Extract the User UUID.
5. Load the corresponding User.
6. Create an `Authentication` object.
7. Store it in the `SecurityContext`.

The filter does **not** perform business logic.

---

# SecurityUserService

NimbusID intentionally does not use Spring Security's `UserDetailsService`.

Spring's default interface assumes username-based authentication.

NimbusID authenticates users using the UUID stored in the JWT subject (`sub`) claim.

Instead, NimbusID provides:

```java
User loadUserById(UUID userId);
```

This aligns the authentication infrastructure with the domain model.

---

# Authentication Object

Once the JWT has been validated, the filter creates an authenticated `Authentication` instance.

Conceptually, the object contains:

```text
Authentication
├── Principal
├── Credentials
├── Authorities
└── Details
```

Current values:

```text
Principal
    User

Credentials
    null

Authorities
    []

Details
    WebAuthenticationDetails
```

The `User` entity becomes the authenticated principal for the remainder of the request.

---

# SecurityContext

Spring Security stores the authenticated user in the `SecurityContext`.

```text
SecurityContext
        │
Authentication
        │
Principal
        │
User
```

Once stored, any component can access the current user through:

* Controller method parameters
* `SecurityContextHolder`
* Spring Security annotations

The `SecurityContext` exists only for the lifetime of the current request.

---

# Current Principal

NimbusID currently stores the domain `User` entity directly as the authenticated principal.

Reasons:

* Simple implementation
* Stateless authentication
* No lazy relationships
* Avoid unnecessary abstraction (YAGNI)

As authorization evolves, this may be replaced with a dedicated `AuthenticatedUser` model.

---

# Authorization

Authentication answers the question:

> **Who is making this request?**

Authorization answers:

> **Is this user allowed to perform this action?**

NimbusID currently performs authentication only.

Authorization using roles and permissions will be introduced in a future milestone.

---

# Current API Endpoints

## Public

```http
POST /api/v1/users
POST /api/v1/auth/login
GET  /api/v1/health
```

## Protected

```http
GET /api/v1/auth/me
```

Protected endpoints require a valid Bearer token.

---

# Future Authentication Enhancements

The authentication pipeline will evolve with additional capabilities, including:

* Refresh token persistence
* Refresh token rotation
* Logout
* Replay attack detection
* Device tracking
* Audit logging
* Multi-factor authentication

These features will extend the current architecture without changing the core authentication flow.

---

# Summary

NimbusID follows a stateless authentication model.

Authentication is divided across three modules:

* **User** manages identity.
* **Auth** verifies credentials and issues tokens.
* **Security** validates tokens and integrates with Spring Security.

This separation keeps business logic independent from infrastructure while providing a clear and maintainable authentication pipeline.
