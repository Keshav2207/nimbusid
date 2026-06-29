# Debugging & Learning Journal

This document captures debugging sessions, implementation notes, architectural discoveries, and lessons learned while building NimbusID.

Its purpose is not to document every bug, but to preserve knowledge that influenced the design of the system.

---
## Why UUID Instead of Long?

### Decision

NimbusID uses UUIDs as primary keys instead of auto-incrementing `long` or `int` identifiers.

### Why?

An Identity & Access Management system frequently operates across multiple services, databases, and clients.

UUIDs provide globally unique identifiers without requiring a centralized ID generator.

This aligns well with distributed system design and modern authentication architectures.

### Advantages

#### Global Uniqueness

UUIDs can be generated anywhere without coordinating with the database.

```text
Application A  → UUID
Application B  → UUID
Application C  → UUID
```

All generated IDs remain unique.

---

#### Better for Distributed Systems

Future NimbusID components may exist as separate services.

Using UUIDs avoids ID collisions when data is created in multiple locations.

---

#### Safer Public APIs

Auto-increment IDs reveal information about the system.

Example:

```text
/users/1
/users/2
/users/3
```

An attacker can easily guess additional identifiers.

UUIDs are significantly harder to enumerate.

```text
/users/550e8400-e29b-41d4-a716-446655440000
```

While UUIDs are **not** a security mechanism, they reduce predictable identifier exposure.

---

#### Easier Data Migration

When importing data between environments or merging databases, UUIDs greatly reduce the chance of primary key conflicts.

---

#### Stable External Identifiers

JWT subject (`sub`) stores the user's UUID.

This identifier remains stable across the system and naturally becomes the canonical identity reference.

---

### Trade-offs

UUIDs are not perfect.

Compared to numeric IDs they:

* Consume more storage.
* Produce larger indexes.
* Are less human-readable.
* Can result in more index fragmentation when randomly generated.

For many business applications these costs are acceptable given the architectural benefits.

---

### Why NimbusID Chose UUID

NimbusID prioritizes architecture and scalability over minimal storage requirements.

Because the platform is intended to evolve into an enterprise IAM system supporting OAuth, SAML, organizations, and distributed services, UUIDs provide a better long-term identity model than sequential numeric identifiers.

This decision also aligns naturally with JWT authentication, where the UUID is stored as the token subject and uniquely identifies the authenticated user across the platform.

# 2026-06 — JWT Authentication Pipeline

## Authentication is More Than JWT Validation

### Problem

Initially it appeared that validating a JWT was sufficient to authenticate a request.

However, protected endpoints continued to behave as unauthenticated until Spring Security recognized the authenticated user.

### Learning

JWT validation and Spring Security authentication are two separate responsibilities.

The application must explicitly create an `Authentication` object and store it in the `SecurityContext`.

Conceptually:

```text
JWT
    │
Validate
    │
Load User
    │
Create Authentication
    │
SecurityContext
    │
Controller
```

Without populating the `SecurityContext`, Spring treats the request as anonymous.

---

## SecurityContext is the Source of Truth

Spring Security does not inspect JWTs throughout the application.

Instead, every authenticated request relies on the `SecurityContext`.

Controllers, filters, and authorization mechanisms all obtain the current user from this context.

Once the `Authentication` object is stored, the remainder of the application works with the authenticated principal rather than the JWT itself.

---

## UsernamePasswordAuthenticationToken Is Generic

Despite its name, `UsernamePasswordAuthenticationToken` is not limited to username/password authentication.

It is Spring Security's general-purpose implementation of the `Authentication` interface.

NimbusID uses it to represent an already authenticated JWT request.

Current values:

* Principal → User
* Credentials → null
* Authorities → empty
* Details → WebAuthenticationDetails

---

## Why SecurityUserService Instead of UserDetailsService?

Spring Security historically assumes username-based authentication through `UserDetailsService`.

NimbusID authenticates users using the UUID stored in the JWT subject (`sub`) claim.

To better reflect the domain, NimbusID introduces:

```java
User loadUserById(UUID userId);
```

This keeps authentication infrastructure aligned with the domain model and avoids forcing username semantics where they are unnecessary.

---

## User Entity as the Principal

The authenticated principal is currently the domain `User` entity.

Reasons:

* Simplicity
* Stateless JWT authentication
* No role or permission model yet
* Avoid premature abstractions (YAGNI)

A dedicated `AuthenticatedUser` model can be introduced naturally when authorization requirements become more complex.

---

## JWT Should Remain Minimal

The current JWT contains only:

* Issuer (`iss`)
* Subject (`sub`)
* Issued At (`iat`)
* Expiration (`exp`)

Additional claims such as roles, permissions, organizations, or scopes should only be introduced when required.

Keeping the token small reduces complexity and minimizes coupling between services.

---

## Security Infrastructure vs Authentication Logic

One of the key architectural decisions was separating authentication business logic from Spring Security infrastructure.

Current ownership:

```text
Auth Module
    Login
    JWT generation
    Refresh tokens

Security Module
    JWT validation
    SecurityFilterChain
    SecurityContext integration

User Module
    User persistence
    Registration
```

This separation keeps modules focused and prevents business logic from leaking into infrastructure components.

---

# General Lessons

* Validate the architecture before adding new features.
* Prefer understanding framework behavior over copying examples.
* Keep JWTs small.
* Avoid introducing abstractions before they solve a real problem.
* Let business domains determine module boundaries.
* Document architectural decisions while they are fresh.

---

# Future Entries

This journal should continue to grow as NimbusID evolves.

Examples of future topics:

* Refresh token rotation
* Replay attack detection
* Authorization model
* Spring Security method security
* OAuth 2.1 implementation
* OpenID Connect
* SAML integration
* Multi-factor authentication
* Performance optimizations
* Production deployment learnings
