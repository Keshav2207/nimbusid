# NimbusID Documentation

Welcome to the NimbusID documentation.

NimbusID is a production-grade Identity & Access Management (IAM) platform built to explore enterprise authentication and authorization concepts while maintaining a clean, domain-driven architecture.

This documentation serves as the project's engineering handbook, capturing not only how NimbusID works, but also why architectural decisions were made.

---

## Documentation Structure

```text
docs/
├── README.md
├── roadmap.md
├── architecture/
├── api/
├── database/
├── development/
└── images/
```

### architecture/

High-level architecture, module responsibilities, authentication flows, and Architecture Decision Records (ADRs).

### api/

API documentation and endpoint behavior.

### database/

Database schema, entity relationships, and migration notes.

### development/

Development setup, project structure, and coding conventions.

### roadmap.md

Current implementation status and future milestones.

### images/

Architecture diagrams and sequence diagrams referenced throughout the documentation.

---

## Documentation Principles

NimbusID documentation follows a few simple principles:

* Keep documentation close to the implementation.
* Document architectural decisions, not obvious code.
* Explain *why* decisions were made, not only *what* was built.
* Keep documents concise and focused on a single topic.
* Update documentation alongside significant architectural changes.

---

## Architecture Decision Records (ADRs)

NimbusID uses Architecture Decision Records to capture important design decisions.

Each ADR includes:

* Context
* Decision
* Consequences

This provides historical context and makes future refactoring decisions easier.

---

## Contributing

When introducing significant architectural changes:

1. Update the relevant documentation.
2. Add or update an ADR if a new architectural decision is made.
3. Keep diagrams and examples synchronized with the implementation.

Documentation is considered part of the project and should evolve together with the codebase.
