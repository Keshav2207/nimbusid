# Architecture Overview

## Introduction

NimbusID is a production-grade Identity & Access Management (IAM) platform built to explore enterprise authentication and authorization while following clean architecture principles.

Rather than implementing every IAM feature upfront, NimbusID evolves incrementally. Each capability is introduced only when the existing architecture naturally requires it.

The objective is to build software that remains understandable as it grows.

---

# Vision

NimbusID aims to provide a modern IAM platform inspired by systems such as Keycloak, Auth0, Okta, and CAS.

The project emphasizes:

* Clean architecture
* Domain-driven module boundaries
* Incremental development
* Secure authentication
* Standards-based identity protocols
* Long-term maintainability

---

# Design Principles

## Domain First

Business domains determine module boundaries.

Frameworks support the domain rather than defining it.

---

## Separation of Responsibilities

Each module owns a single business capability.

Examples:

* User identity belongs to the User module.
* Authentication belongs to the Auth module.
* Security infrastructure belongs to the Security module.

---

## Incremental Architecture

NimbusID intentionally avoids speculative abstractions.

New abstractions are introduced only when justified by real requirements.

Examples include:

* Introducing an `AuthenticatedUser` only after roles or permissions require it.
* Delaying OAuth2 until the authentication model is stable.
* Keeping JWT claims minimal until additional information is needed.

---

## Stateless Authentication

NimbusID currently authenticates API requests using JSON Web Tokens (JWT).

Each request contains all information required to authenticate the caller without relying on server-side HTTP sessions.

---

## Production-Oriented Design

Although NimbusID is a learning project, architectural decisions aim to reflect production systems wherever practical.

Examples include:

* UUID primary keys
* BCrypt password hashing
* Database migrations using Flyway
* Layered module boundaries
* Stateless authentication
* Refresh token rotation (planned)

---

# High-Level Architecture

```text
                   Clients
                       │
                       ▼
              Spring Boot Application
                       │
        ┌──────────────┴──────────────┐
        │                             │
        ▼                             ▼
 Authentication                User Management
        │                             │
        └──────────────┬──────────────┘
                       ▼
                Shared Platform
                       │
                       ▼
                 PostgreSQL Database
```

---

# Technology Stack

| Component      | Technology        |
| -------------- | ----------------- |
| Language       | Java 17           |
| Framework      | Spring Boot 4     |
| Security       | Spring Security   |
| Database       | PostgreSQL        |
| Migration      | Flyway            |
| Build Tool     | Maven             |
| Authentication | JWT               |
| Documentation  | springdoc-openapi |

---

# Module Architecture

NimbusID is organized as a Maven multi-module project.

```text
nimbusid
│
├── nimbusid-app
├── nimbusid-platform
├── nimbusid-security
├── nimbusid-user
├── nimbusid-auth
├── docs
├── docker
└── .github
```

Each module has a clearly defined responsibility and communicates through well-defined interfaces.

Further details are documented in **modules.md**.

---

# Authentication Pipeline

Authentication follows a stateless request model.

```text
Client Request
      │
Bearer JWT
      │
JwtAuthenticationFilter
      │
JwtTokenService
      │
SecurityUserService
      │
User
      │
SecurityContext
      │
Controller
```

This pipeline authenticates every protected request before application code is executed.

A detailed walkthrough is available in **authentication-flow.md**.

---

# Future Architecture

NimbusID is designed to evolve incrementally.

Planned additions include:

* Refresh token lifecycle
* Authorization (roles and permissions)
* OAuth 2.1 Authorization Server
* OpenID Connect
* SAML 2.0 Identity Provider
* Multi-factor authentication
* Multi-tenancy
* Audit logging

These capabilities will be introduced without compromising existing module boundaries.

---

# Documentation Structure

The architecture documentation is organized as follows:

* **overview.md** — High-level architecture and design philosophy.
* **modules.md** — Responsibilities of each module.
* **authentication-flow.md** — Authentication request lifecycle.
* **security.md** — Security infrastructure and implementation details.
* **decisions/** — Architecture Decision Records (ADRs).

Together, these documents describe both how NimbusID is implemented and why architectural decisions were made.
