# NimbusID Roadmap

This roadmap outlines the evolution of NimbusID from a minimal authentication service into a production-grade Identity & Access Management (IAM) platform.

The roadmap is intentionally incremental. Each milestone builds upon a stable foundation while avoiding unnecessary abstractions until they are justified.

---

# Guiding Principles

* Build features incrementally.
* Keep module responsibilities clear.
* Favor simplicity over premature abstraction (YAGNI).
* Let the domain drive the architecture.
* Prioritize maintainability over feature count.

---

# Current Status

## Foundation

* ✅ Maven multi-module architecture
* ✅ Spring Boot application bootstrap
* ✅ PostgreSQL integration
* ✅ Flyway database migrations
* ✅ UUID primary keys
* ✅ OpenAPI / Swagger integration
* ✅ Shared platform module
* ✅ Response framework

---

## User Management

* ✅ User entity
* ✅ User persistence
* ✅ User registration
* ✅ Password hashing (BCrypt)

---

## Authentication

* ✅ Login API
* ✅ JWT access token generation
* ✅ JWT refresh token generation
* ✅ JWT validation
* ✅ JWT authentication filter
* ✅ Protected endpoints
* ✅ Current user endpoint (`/api/v1/auth/me`)
* ✅ Swagger Bearer authentication

---

# Next Milestone

## Refresh Token Lifecycle

Implement persistent refresh tokens with secure rotation.

Planned work:

* ⬜ RefreshToken entity
* ⬜ RefreshToken repository
* ⬜ RefreshToken service
* ⬜ Persist refresh tokens during login
* ⬜ `/api/v1/auth/refresh`
* ⬜ Refresh token rotation
* ⬜ Refresh token revocation
* ⬜ Logout endpoint

---

# Upcoming Milestones

## Authentication Hardening

* ⬜ Refresh token replay detection
* ⬜ Login audit events
* ⬜ Session management
* ⬜ Remember device support
* ⬜ Device metadata
* ⬜ Security event logging

---

## Authorization

Introduce authorization on top of authentication.

Planned features:

* ⬜ Roles
* ⬜ Permissions
* ⬜ Granted authorities
* ⬜ Method security
* ⬜ Resource authorization

---

## OAuth 2.1 Authorization Server

Planned capabilities:

* ⬜ OAuth clients
* ⬜ Authorization Code flow
* ⬜ Client Credentials flow
* ⬜ PKCE
* ⬜ Token introspection
* ⬜ Token revocation
* ⬜ JWK endpoint

---

## OpenID Connect

* ⬜ ID Tokens
* ⬜ UserInfo endpoint
* ⬜ Discovery endpoint
* ⬜ JWKS endpoint

---

## SAML 2.0

* ⬜ Identity Provider (IdP)
* ⬜ Service Provider integration
* ⬜ Metadata endpoint
* ⬜ SAML login
* ⬜ Single Logout

---

## Identity Management

* ⬜ Email verification
* ⬜ Password reset
* ⬜ Password policies
* ⬜ Account lockout
* ⬜ Account recovery

---

## Multi-Factor Authentication

* ⬜ TOTP
* ⬜ Backup codes
* ⬜ Recovery flow

---

## Administration

* ⬜ User management
* ⬜ Role management
* ⬜ Permission management
* ⬜ Client management
* ⬜ Audit logs

---

## Multi-Tenancy

* ⬜ Organizations
* ⬜ Tenants
* ⬜ Isolation model

---

## Developer Experience

* ⬜ Integration tests
* ⬜ Unit tests
* ⬜ Testcontainers
* ⬜ Docker Compose improvements
* ⬜ GitHub Actions CI
* ⬜ Code coverage
* ⬜ Static analysis

---

# Long-Term Vision

NimbusID aims to become a complete Identity & Access Management platform supporting modern authentication standards while remaining modular, understandable, and suitable for enterprise applications.

The emphasis is not only on feature completeness but also on demonstrating clean architecture, sound engineering practices, and well-documented design decisions.
