# Module Architecture

## Introduction

NimbusID is organized as a Maven multi-module project.

Each module owns a specific business capability or infrastructure concern. Module boundaries are intentionally kept strict to reduce coupling and improve maintainability.

A module should have a single, well-defined responsibility. Business logic should remain inside the module that owns the domain.

---

# Module Overview

```text
nimbusid
│
├── nimbusid-app
├── nimbusid-platform
├── nimbusid-security
├── nimbusid-user
├── nimbusid-auth
│
├── docs
├── docker
└── .github
```

Future modules:

```text
nimbusid-oauth
nimbusid-saml
```

---

# Dependency Direction

```text
                 nimbusid-app
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ▼             ▼             ▼
  nimbusid-auth   nimbusid-user   nimbusid-security
        │               │
        └───────┬───────┘
                ▼
        nimbusid-platform
```

The application module composes the system.

Business modules may depend on shared infrastructure provided by the platform module.

Modules should not become tightly coupled to one another.

---

# Module Responsibilities

## nimbusid-app

### Purpose

Application composition root.

This module wires together all other modules and starts the Spring Boot application.

### Responsibilities

* Spring Boot bootstrap
* Component scanning
* Configuration
* Dependency composition
* Application startup

### Does Not Contain

* Business logic
* Authentication logic
* User management
* Shared utilities

---

## nimbusid-platform

### Purpose

Shared infrastructure used throughout the application.

### Responsibilities

* Response models
* ResponseFactory
* Shared configuration
* Infrastructure beans
* Common utilities

Current contents include:

```text
response/
factory/
config/
```

Examples:

* ApiResponse
* ApiErrorResponse
* ResponseFactory
* Clock bean

### Does Not Contain

* User logic
* Authentication
* Security filters
* Business rules

---

## nimbusid-user

### Purpose

Owns user identity.

The User module is responsible for managing user data throughout its lifecycle.

### Responsibilities

* User persistence
* Registration
* User lookup
* User lifecycle
* Password changes (future)
* User administration (future)

### Does Not Contain

* Login
* JWT generation
* Refresh tokens
* Authentication rules

Authentication belongs to the Auth module.

---

## nimbusid-auth

### Purpose

Owns authentication.

The Auth module verifies user credentials and issues authentication tokens.

### Responsibilities

* Login
* JWT generation
* Refresh token lifecycle
* Current user endpoint
* Logout (future)

### Current APIs

```text
POST /api/v1/auth/login
GET  /api/v1/auth/me
```

### Future APIs

```text
POST /api/v1/auth/refresh
POST /api/v1/auth/logout
```

### Does Not Contain

* User persistence
* Security filter chain
* Password encoding configuration

---

## nimbusid-security

### Purpose

Provides security infrastructure.

This module integrates NimbusID with Spring Security while remaining independent of authentication business logic.

### Responsibilities

* SecurityFilterChain
* JWT authentication filter
* PasswordEncoder
* Security adapters
* Authentication infrastructure

Current package structure:

```text
config/
filter/
service/
```

### Does Not Contain

* Login
* Registration
* JWT generation
* Business validation

Security infrastructure and authentication business logic remain separate.

---

# Current Authentication Ownership

Authentication responsibilities are intentionally divided.

```text
User Module

    User persistence
    Registration
    User lookup

            │

            ▼

Auth Module

    Login
    JWT creation
    Refresh tokens
    Logout

            │

            ▼

Security Module

    JWT validation
    SecurityContext population
    Spring Security integration
```

This separation prevents authentication concerns from leaking into unrelated modules.

---

# Future Modules

## nimbusid-oauth

Will implement OAuth 2.1 Authorization Server capabilities.

Planned responsibilities:

* OAuth clients
* Authorization Code flow
* PKCE
* Client Credentials
* Token introspection
* Token revocation

---

## nimbusid-saml

Will implement SAML 2.0 Identity Provider functionality.

Planned responsibilities:

* Identity Provider (IdP)
* Metadata endpoint
* SAML authentication
* Single Logout

---

# Design Guidelines

When introducing new functionality, consider the following questions:

1. Which business domain owns this feature?
2. Does an existing module already have this responsibility?
3. Is a new module justified?
4. Is the feature infrastructure or business logic?

New functionality should be placed in the module that naturally owns the responsibility.

---

# Summary

NimbusID favors small, focused modules with explicit ownership.

The architecture follows a simple principle:

* Business modules own business logic.
* Infrastructure modules provide technical capabilities.
* The application module composes everything together.

Maintaining these boundaries keeps the project understandable as additional capabilities such as authorization, OAuth 2.1, OpenID Connect, and SAML are introduced.
