# FlowForge Full-Stack Development Backlog

> Production-style, full-stack subscription and service-fulfillment platform with a Next.js customer portal and operations console backed by event-driven Spring Boot microservices, Kafka, PostgreSQL, Redis, Docker, Kubernetes, and OpenTelemetry.

---

## 1. Document Purpose

This file is the single full-stack execution backlog for human developers and coding agents.

It defines:

- Product scope and architecture boundaries
- Delivery milestones
- Epics, stories, tasks, and dependencies
- Acceptance criteria and required evidence
- Coding-agent operating rules
- Definition of Ready and Definition of Done
- Frontend UX, testing, accessibility, security, observability, and documentation expectations
- Handoff and pull-request conventions

Do not treat this file as a loose roadmap. Every frontend, backend, contract, infrastructure, and documentation change must map to a backlog ID.

---

## 2. Project Objectives

FlowForge demonstrates a complete customer and operator experience for a distributed subscription workflow. Customers browse products, place and track orders, manage subscriptions, and request cancellation through a Next.js application. Operations users investigate failures, replay dead-letter messages, recover stuck workflows, and control provider migration. Spring Boot microservices remain the owners of business data and rules.

The project must demonstrate:

1. A polished, responsive Next.js customer portal and operations console
2. React Server Components by default with deliberate client-component boundaries
3. Secure server-side authentication and role-based authorization with Keycloak
4. A browser-to-Next.js-to-gateway integration model that keeps access tokens out of browser storage
5. Type-safe frontend integration generated from backend OpenAPI contracts
6. Accessible forms, navigation, tables, feedback, and error states
7. Clean Spring Boot service design and explicit domain boundaries
8. Event-driven communication and Saga orchestration
9. Transactional outbox publishing and idempotent commands, events, and callbacks
10. External-provider anti-corruption layers and controlled legacy-provider migration
11. Resilience, compensation, reconciliation, and failure recovery
12. End-to-end observability across browser, Next.js, gateway, Kafka, and microservices
13. Automated unit, component, contract, browser, integration, and failure-path testing
14. Containerized local development and Kubernetes deployment
15. CI/CD quality gates for Java, TypeScript, containers, contracts, and infrastructure
16. Clear architecture documentation and reproducible full-stack demos

### 2.1 Runtime Architecture

```text
Browser
  |
  | HTTPS, same-origin cookies
  v
Next.js Web Application
  |- Customer Portal
  |- Operations Console
  |- Server Components
  |- Route Handlers / BFF
  |- Auth.js session integration
  |
  | OAuth access token, correlation ID
  v
Spring Cloud API Gateway
  |
  +--> Customer Service
  +--> Catalog Service
  +--> Order Service
  +--> Operations APIs
           |
           v
       Kafka backbone
   +-------+--------+
   |       |        |
Payment  Provisioning  Notification
   |       |
Provider simulators
```

**Boundary rules**

- The browser calls only the Next.js origin for application data.
- The Next.js server calls the Spring Cloud Gateway; it never reads service databases.
- Spring Boot services remain the source of truth for authorization-sensitive business rules.
- The web layer may aggregate and reshape responses but must not duplicate domain state machines.
- Access and refresh tokens must not be written to `localStorage` or exposed to browser JavaScript.
- Correlation IDs must cross the complete browser-to-service request path.

---

## 3. Core Technology Decisions

| Area | Decision |
|---|---|
| Backend language | Java 21 |
| Backend framework | Spring Boot 3.5.x |
| Backend build | Gradle Kotlin DSL |
| Web framework | Next.js App Router, exact stable version pinned in lockfile |
| Web language | TypeScript with strict mode |
| React architecture | Server Components by default; Client Components only for browser interaction |
| Web package manager | pnpm with committed lockfile |
| UI styling | Tailwind CSS with accessible headless component primitives |
| Forms and validation | React Hook Form and Zod; backend validation remains authoritative |
| Client data synchronization | Server-side `fetch` for initial rendering; TanStack Query for interactive polling and mutations |
| Web authentication | Auth.js with Keycloak OIDC, server-managed secure cookies |
| Browser/backend integration | Next.js Route Handlers and server-side functions as a BFF over Spring Cloud Gateway |
| API client generation | OpenAPI-derived TypeScript types and client functions |
| API | REST, JSON, OpenAPI 3 |
| Messaging | Apache Kafka |
| Database | PostgreSQL |
| Migrations | Flyway |
| Cache and distributed coordination | Redis |
| Identity provider | Keycloak, OAuth 2.0, OpenID Connect |
| Resilience | Resilience4j |
| Backend testing | JUnit 5, Mockito, AssertJ, Testcontainers, WireMock |
| Web unit/component testing | Vitest, React Testing Library, user-event |
| Browser testing | Playwright |
| Accessibility testing | axe-core plus manual keyboard and screen-reader checks |
| Contract testing | Spring Cloud Contract or Pact |
| Performance testing | k6 for APIs and Lighthouse-based web performance checks |
| Observability | OpenTelemetry, Prometheus, Grafana, Loki |
| Local runtime | Native Next.js development plus Docker Compose dependencies |
| Orchestration | Kubernetes |
| Packaging | Helm and production Docker images |
| CI/CD | GitHub Actions |
| API documentation | springdoc-openapi |
| Static analysis | SonarQube or SonarCloud, Checkstyle, SpotBugs, ESLint, TypeScript |
| Container scanning | Trivy |
| Dependency scanning | OWASP Dependency-Check and JavaScript dependency audit tooling |

Versions must be pinned in Gradle catalogs, `package.json`, and `pnpm-lock.yaml`. Any change to a core decision requires an Architecture Decision Record.

---

## 4. Repository Layout

```text
flowforge/
├── README.md
├── BACKLOG.md
├── AGENTS.md
├── CHANGELOG.md
├── CODEOWNERS
├── package.json
├── pnpm-lock.yaml
├── pnpm-workspace.yaml
├── apps/
│   └── web/
│       ├── AGENTS.md
│       ├── app/
│       │   ├── (public)/
│       │   ├── (customer)/
│       │   ├── (operations)/
│       │   ├── api/
│       │   ├── error.tsx
│       │   ├── global-error.tsx
│       │   ├── loading.tsx
│       │   └── layout.tsx
│       ├── components/
│       │   ├── ui/
│       │   ├── customer/
│       │   ├── operations/
│       │   └── shared/
│       ├── features/
│       │   ├── auth/
│       │   ├── catalog/
│       │   ├── checkout/
│       │   ├── orders/
│       │   ├── subscriptions/
│       │   ├── operations/
│       │   └── migration/
│       ├── lib/
│       │   ├── api/
│       │   ├── auth/
│       │   ├── observability/
│       │   └── validation/
│       ├── public/
│       ├── tests/
│       ├── e2e/
│       ├── next.config.ts
│       ├── playwright.config.ts
│       └── package.json
├── docs/
│   ├── architecture.md
│   ├── frontend-architecture.md
│   ├── design-system.md
│   ├── user-journeys.md
│   ├── local-development.md
│   ├── testing.md
│   ├── operations.md
│   ├── security.md
│   ├── migration-strategy.md
│   ├── failure-scenarios.md
│   ├── performance-results.md
│   ├── demo-script.md
│   ├── api/
│   ├── events/
│   ├── diagrams/
│   └── adr/
├── libraries/
│   ├── event-envelope/
│   ├── observability-starter/
│   └── test-support/
├── services/
│   ├── api-gateway/
│   ├── customer-service/
│   ├── catalog-service/
│   ├── order-service/
│   ├── payment-service/
│   ├── provisioning-service/
│   └── notification-service/
├── provider-simulators/
│   ├── payment-provider-simulator/
│   ├── legacy-provider-simulator/
│   └── nextgen-provider-simulator/
├── platform/
│   ├── docker-compose/
│   ├── keycloak/
│   ├── kafka/
│   ├── kubernetes/
│   ├── helm/
│   ├── observability/
│   └── scripts/
├── testing/
│   ├── contract-tests/
│   ├── end-to-end/
│   ├── performance/
│   └── test-data/
└── .github/
    ├── ISSUE_TEMPLATE/
    ├── pull_request_template.md
    └── workflows/
```

Shared Java libraries must contain technical infrastructure only. Frontend feature modules may share presentation utilities, generated contracts, and design-system components, but must not reproduce backend domain rules.

---

## 5. Delivery Principles

1. Prefer small, independently reviewable vertical slices that include UI, API integration, backend behavior, and tests where applicable.
2. Do not merge incomplete public behavior behind undocumented assumptions.
3. Render on the server by default; add client-side JavaScript only for required interaction.
4. Keep authentication tokens server-side and use secure, HTTP-only cookies for the web session.
5. The browser must not call individual microservices directly.
6. The frontend must consume generated or contract-checked API types rather than handwritten duplicate DTOs.
7. Frontend validation improves usability; backend validation remains authoritative.
8. Every customer-visible async action must show pending, success, failure, retry, and timeout states.
9. Every asynchronous consumer must be idempotent.
10. Every external side effect must have a stable business reference.
11. Every database schema change must use Flyway.
12. Every public API or event contract must be versioned.
13. Every service and the web runtime must expose health, metrics, logs, and traces where technically applicable.
14. Failure paths are first-class functionality.
15. Never claim exactly-once delivery across the full system.
16. Do not fabricate performance, accessibility, or reliability numbers.
17. Keep infrastructure and frontend builds reproducible from source control and lockfiles.
18. No service or frontend runtime may read another service's database.
19. No sensitive values may be committed to the repository or exposed through `NEXT_PUBLIC_*` variables.
20. Avoid premature abstraction; extract shared code only after proven duplication.
21. Keep the complete customer journey runnable throughout development.

---

## 6. Status and Priority Model

### Status

| Status | Meaning |
|---|---|
| `DRAFT` | Not ready for implementation |
| `READY` | Acceptance criteria and dependencies are complete |
| `IN_PROGRESS` | Assigned and actively implemented |
| `BLOCKED` | Cannot proceed; blocker documented |
| `IN_REVIEW` | Pull request is open |
| `DONE` | Merged and all completion evidence exists |
| `DEFERRED` | Deliberately postponed |

### Priority

| Priority | Meaning |
|---|---|
| `P0` | Required to keep the system buildable or safe |
| `P1` | Required for the portfolio-ready release |
| `P2` | Important production-readiness improvement |
| `P3` | Optional differentiation or stretch scope |

### Estimates

Story-point guidance:

| Points | Expected scope |
|---:|---|
| 1 | Very small, localized change |
| 2 | Small change with tests |
| 3 | Normal feature in one component |
| 5 | Cross-layer feature or moderate integration |
| 8 | Multi-component or high-risk feature |
| 13 | Too large; must be decomposed before implementation |

No `READY` item may exceed 8 points.

---

## 7. Milestones

| Milestone | Goal | Required Epics |
|---|---|---|
| M0 | Repository, local platform, and web foundation | EPIC-00, EPIC-01, EPIC-20 |
| M1 | Synchronous full-stack customer journey | EPIC-02, EPIC-03, EPIC-04, EPIC-05, EPIC-21, EPIC-22 |
| M2 | Event-driven fulfillment workflow | EPIC-06, EPIC-07, EPIC-08, EPIC-09, EPIC-10 |
| M3 | Reliability, observability, and operations console | EPIC-11, EPIC-12, EPIC-13, EPIC-23 |
| M4 | Security and full-stack verification | EPIC-14, EPIC-15, EPIC-24 |
| M5 | Kubernetes and delivery automation | EPIC-16, EPIC-17, EPIC-25 |
| M6 | Performance and migration showcase | EPIC-18, EPIC-19 |
| M7 | Portfolio release | EPIC-26 |

---

# 8. Backlog

## EPIC-00 — Repository Governance and Engineering Standards

**Goal:** Establish a predictable workflow for developers and coding agents.

**Exit criteria:**

- Repository builds from a clean checkout
- Contribution and agent rules are documented
- Pull requests have standard validation
- All modules use consistent build and quality conventions

### FF-0001 — Initialize monorepo

- **Status:** IN_REVIEW
- **Priority:** P0
- **Estimate:** 3
- **Dependencies:** None
- **Owner:** Unassigned

**Scope**

Create the root Gradle project, module layout, wrapper, `.editorconfig`, `.gitignore`, license, and baseline README.

**Acceptance criteria**

- `./gradlew clean build` succeeds from repository root.
- Java toolchain is pinned to Java 21.
- Every planned service and the Next.js application have a placeholder module or documented creation ticket.
- Build output and IDE files are ignored.
- README contains prerequisites and the initial build command.

**Required tests/evidence**

- Clean-checkout build log
- Repository tree included in the pull request

**Agent notes**

Do not add domain implementations in this ticket.

---

### FF-0002 — Add common Gradle conventions

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 3
- **Dependencies:** FF-0001

**Scope**

Create convention plugins for Java, Spring Boot, testing, code coverage, formatting, and static analysis.

**Acceptance criteria**

- All Java modules share compiler and test configuration.
- Warnings are treated consistently.
- JUnit 5 is enabled.
- JaCoCo XML and HTML reports are generated.
- Checkstyle or equivalent is executed by `check`.
- No backend service duplicates root-level dependency versions; JavaScript versions are locked through the root pnpm workspace.

---

### FF-0003 — Create contribution workflow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 2
- **Dependencies:** FF-0001

**Scope**

Add `CONTRIBUTING.md`, branch naming, commit conventions, pull-request template, and issue templates.

**Acceptance criteria**

- Branch format is `feature/FF-####-description`, `fix/FF-####-description`, or `chore/FF-####-description`.
- Commit format is documented.
- Pull-request template requires backlog ID, summary, test evidence, risk, migration impact, and rollback notes.
- Bug and feature issue templates exist.

---

### FF-0004 — Create AGENTS.md

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 2
- **Dependencies:** FF-0001

**Scope**

Create repository-level instructions for Codex and other coding agents.

**Acceptance criteria**

- Agent setup, allowed commands, prohibited changes, validation requirements, and handoff format are documented.
- Instructions require agents to read this backlog before coding.
- Instructions prohibit unrelated refactors.
- Instructions define when an agent must stop and request human review.
- Nested `AGENTS.md` files are permitted for service-specific rules.

---

### FF-0005 — Add architecture-decision process

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 2
- **Dependencies:** FF-0001

**Acceptance criteria**

- ADR template exists under `docs/adr/`.
- Initial ADR index exists.
- Statuses include Proposed, Accepted, Superseded, and Rejected.
- Pull-request template links architectural changes to an ADR.

---

## EPIC-01 — Local Platform Foundation

**Goal:** Provide a reproducible local platform and developer experience.

### FF-0101 — Create base Docker Compose stack

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 5
- **Dependencies:** FF-0001

**Scope**

Provision PostgreSQL, Kafka, Redis, Keycloak, Prometheus, Grafana, OpenTelemetry Collector, and Loki, with networking suitable for both the Next.js application and Spring Boot services.

**Acceptance criteria**

- `docker compose up -d` starts all dependencies.
- Every container has a health check.
- Named volumes preserve required local data.
- Default ports are documented and configurable.
- Services share a dedicated network.
- Local credentials are development-only and clearly labeled.
- `docker compose down -v` returns the environment to a clean state.

**Required evidence**

- `docker compose ps`
- Health-check output
- Screenshot or exported status report

---

### FF-0102 — Create local environment bootstrap scripts

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0101

**Acceptance criteria**

- Scripts exist for start, stop, reset, logs, and dependency checks.
- Scripts work on Linux and macOS or provide documented alternatives.
- Failure messages identify missing dependencies.
- Reset requires an explicit destructive confirmation flag.

---

### FF-0103 — Configure local DNS and service ports

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 2
- **Dependencies:** FF-0101

**Acceptance criteria**

- Every service and dependency has a documented hostname and port.
- Port collisions can be resolved through environment variables.
- Compose service names are used for inter-container communication.

---

### FF-0104 — Seed Keycloak development realm

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0101

**Acceptance criteria**

- Realm import is automated.
- Roles exist: `CUSTOMER`, `SUPPORT_AGENT`, `OPERATIONS`, `ADMIN`.
- Development users exist for each role.
- Service clients exist for internal APIs.
- Secrets are configurable and not hard-coded in source code.

---

## EPIC-02 — API Gateway

**Goal:** Provide a secure, observable external entry point.

### FF-0201 — Bootstrap API Gateway

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0101

**Acceptance criteria**

- Gateway starts with a dedicated configuration profile and accepts authenticated calls from the Next.js BFF.
- Routes can be configured without recompilation.
- Actuator health and metrics are exposed.
- Structured logs include service name and correlation ID.

---

### FF-0202 — Implement correlation-ID propagation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0201

**Acceptance criteria**

- Gateway accepts `X-Correlation-Id` or generates one.
- Invalid identifiers are replaced safely.
- Correlation ID is forwarded downstream.
- Correlation ID is returned in the response.
- Logs and traces include the same value.
- Unit and integration tests cover present and absent headers.

---

### FF-0203 — Configure OAuth 2.0 resource-server security

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0104, FF-0201

**Acceptance criteria**

- Unauthenticated protected requests return 401.
- Authenticated but unauthorized requests return 403.
- Token issuer and audience are validated.
- Public health endpoints remain accessible.
- Role claims are mapped consistently.

---

### FF-0204 — Add Redis-backed rate limiting

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-0201, FF-0101

**Acceptance criteria**

- Limits can be configured per route.
- Client identity derives from authenticated subject and trusted forwarded metadata from the Next.js server where available.
- Rejected requests return 429 with retry metadata.
- Rate-limit metrics are exported.
- Integration tests prove enforcement and reset behavior.

---

### FF-0205 — Standardize gateway error responses

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0201

**Response model**

```json
{
  "code": "RATE_LIMIT_EXCEEDED",
  "message": "Request limit exceeded",
  "correlationId": "corr-123",
  "timestamp": "2026-08-03T08:00:00Z",
  "details": []
}
```

**Acceptance criteria**

- Gateway errors follow the shared format.
- Internal exception messages are not exposed.
- Correlation IDs are always included.
- Error-code documentation exists.

---

## EPIC-03 — Customer Service

**Goal:** Own customer profiles, status, and eligibility-related state.

### FF-0301 — Bootstrap Customer Service

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0101

**Acceptance criteria**

- Service has isolated PostgreSQL schema or database.
- Flyway runs automatically.
- Service exposes health, readiness, metrics, and OpenAPI.
- Base package structure follows documented conventions.

---

### FF-0302 — Create customer domain model

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0301

**Minimum fields**

- `customerId`
- `externalReference`
- `fullName`
- `email`
- `phoneNumber`
- `status`
- `version`
- `createdAt`
- `updatedAt`

**Acceptance criteria**

- UUID is used as the internal identifier.
- External reference and email have unique constraints.
- Status transitions are validated by the domain.
- Optimistic locking is enabled.
- Sensitive values are masked in logs.

---

### FF-0303 — Implement customer REST APIs

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0302

**Endpoints**

```http
POST  /api/v1/customers
GET   /api/v1/customers/{customerId}
PATCH /api/v1/customers/{customerId}/status
GET   /api/v1/customers/{customerId}/subscriptions
```

**Acceptance criteria**

- Validation errors return documented error codes.
- Duplicate creation returns 409.
- Missing customer returns 404.
- Pagination is used for subscriptions.
- OpenAPI examples are included.
- Controller, service, repository, and integration tests exist.

---

### FF-0304 — Add customer authorization rules

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0203, FF-0303

**Acceptance criteria**

- Customer role can read only its mapped customer record.
- Support and admin roles can read any customer.
- Only authorized administrative roles can suspend customers.
- Authorization tests cover allow and deny paths.

---

## EPIC-04 — Catalog Service

**Goal:** Own products, prices, add-ons, eligibility rules, and capacity reservations.

### FF-0401 — Bootstrap Catalog Service

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0101

---

### FF-0402 — Implement versioned product catalog

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0401

**Acceptance criteria**

- Product versions use effective start and end timestamps.
- Activated prices are immutable.
- New prices create a new product version.
- Overlapping effective periods are rejected.
- Query returns the version effective at a requested timestamp.
- Database constraints and service validation both enforce invariants.

---

### FF-0403 — Implement catalog query APIs

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0402

**Endpoints**

```http
GET /api/v1/products
GET /api/v1/products/{productCode}
GET /internal/v1/products/{productCode}/validation
```

**Acceptance criteria**

- Collection endpoint is paginated.
- Filters include product type and effective date.
- Internal validation returns normalized eligibility and pricing data.
- Cache headers are documented.

---

### FF-0404 — Implement capacity reservation model

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0402

**Acceptance criteria**

- Reservation has expiry time and status.
- Capacity cannot become negative.
- Duplicate reservation commands are idempotent.
- Expired reservations can be released.
- Concurrency tests prove that overselling does not occur.

---

### FF-0405 — Add Redis product cache

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0403, FF-0101

**Acceptance criteria**

- Cache keys are versioned.
- TTL is configurable.
- Product publication invalidates affected keys.
- Cache failures degrade to PostgreSQL.
- Cache hit, miss, and error metrics are emitted.

---

## EPIC-05 — Order Service Core

**Goal:** Own orders, state transitions, timelines, and workflow coordination.

### FF-0501 — Bootstrap Order Service

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0101

---

### FF-0502 — Implement order aggregate and state machine

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-0501

**States**

```text
CREATED
VALIDATING
RESERVING
PAYMENT_PENDING
PROVISIONING
ACTIVATING
COMPLETED
COMPENSATING
CANCELLED
FAILED
```

**Acceptance criteria**

- Allowed transitions are explicitly defined.
- Invalid transitions produce a domain error.
- Every transition records actor, reason, timestamp, and correlation ID.
- Aggregate uses optimistic locking.
- Domain tests cover every allowed and denied transition.
- Terminal states cannot be modified except through explicitly documented administrative recovery.

---

### FF-0503 — Implement order creation API

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0502, FF-0303, FF-0403

**Endpoint**

```http
POST /api/v1/orders
```

**Acceptance criteria**

- `Idempotency-Key` is required.
- Request validates customer, product, add-ons, and address reference format.
- Accepted request returns 202 with order resource location.
- Repeated request with same key and same payload returns original order.
- Repeated key with different payload returns 409.
- Synchronous downstream timeouts are bounded.
- API contract and integration tests exist.

---

### FF-0504 — Implement order query and timeline APIs

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0502

**Endpoints**

```http
GET /api/v1/orders/{orderId}
GET /api/v1/orders/{orderId}/timeline
```

**Acceptance criteria**

- Order view contains current state and summarized failure information.
- Timeline is ordered and immutable.
- Authorization restricts customer access to owned orders.
- Support and operations roles can inspect all orders.
- Query performance is covered by indexes and an explain plan.

---

### FF-0505 — Implement order cancellation API

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0502

**Acceptance criteria**

- Cancellation is accepted only from documented states.
- Duplicate cancellation requests are idempotent.
- Cancellation starts compensation when side effects already exist.
- Conflicting completion and cancellation are handled with optimistic locking.
- Concurrency integration tests exist.

---

### FF-0506 — Implement manual retry API

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0502

**Endpoint**

```http
POST /api/v1/orders/{orderId}/retry
```

**Acceptance criteria**

- Only operations role can retry.
- Retry is allowed only for retryable failure categories.
- Retry action is audited.
- Original failure context remains visible.
- Duplicate retry request does not create duplicate side effects.

---

## EPIC-06 — Event Contracts and Kafka Foundation

**Goal:** Establish stable, versioned asynchronous communication.

### FF-0601 — Create event-envelope library

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002

**Envelope fields**

- `eventId`
- `eventType`
- `eventVersion`
- `aggregateId`
- `correlationId`
- `causationId`
- `occurredAt`
- `producer`
- `payload`
- `metadata`

**Acceptance criteria**

- Serialization format is documented.
- Unknown metadata fields are tolerated.
- Event IDs are UUIDs.
- Timestamp format is UTC ISO-8601.
- Unit tests prove serialization compatibility.

---

### FF-0602 — Define topic naming and partitioning

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 2
- **Dependencies:** FF-0601

**Acceptance criteria**

- Topic naming convention is documented.
- `orderId` is the partition key for workflow events.
- Retention and partition defaults are documented.
- Dead-letter topic naming is defined.
- ADR explains ordering and scaling trade-offs.

---

### FF-0603 — Define initial event catalog

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0601

**Minimum events**

- `CustomerCreated`
- `CustomerStatusChanged`
- `CapacityReserved`
- `CapacityReservationFailed`
- `CapacityReleased`
- `PaymentAuthorized`
- `PaymentDeclined`
- `PaymentCaptured`
- `RefundCompleted`
- `RefundFailed`
- `ProvisioningStarted`
- `ProvisioningCompleted`
- `ProvisioningFailed`
- `ProvisioningCancelled`
- `OrderCompleted`
- `OrderFailed`
- `NotificationRequested`

**Acceptance criteria**

- Each event has JSON schema and example payload.
- Required versus optional fields are documented.
- Compatibility rules are documented.
- Schema version 1 is stored under `docs/events/`.

---

### FF-0604 — Add Kafka development configuration

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0101, FF-0602

**Acceptance criteria**

- Topics are created reproducibly.
- Producer acknowledgements and retry settings are explicit.
- Consumer groups follow a documented naming convention.
- Local developer tooling can inspect topics and messages.
- Sensitive payload fields are prohibited by documentation and tests where practical.

---

## EPIC-07 — Transactional Outbox and Idempotent Consumption

**Goal:** Ensure reliable database-to-Kafka publication and duplicate-safe processing.

### FF-0701 — Implement reusable outbox pattern

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-0601

**Acceptance criteria**

- Aggregate update and outbox insert occur in one database transaction.
- Outbox record stores event type, version, payload, correlation, status, and retry count.
- Publisher uses safe row claiming.
- Multiple publisher replicas do not publish the same row concurrently.
- Published rows retain audit information.
- Failed publication is retried with bounded backoff.
- Metrics expose pending, failed, and oldest-event age.
- Integration tests stop Kafka, create transactions, restore Kafka, and verify eventual publication.

---

### FF-0702 — Implement processed-event store

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0601

**Acceptance criteria**

- Consumers persist processed event IDs.
- Duplicate events return success without reapplying side effects.
- Processing and business database changes share one transaction when possible.
- Retention strategy is documented.
- Duplicate-delivery integration tests exist.

---

### FF-0703 — Add outbox reconciliation job

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0701

**Acceptance criteria**

- Job detects stale pending and failed records.
- Job can safely retry eligible records.
- Job emits metrics and structured logs.
- Job uses a distributed lock when multiple replicas run.
- Operations documentation includes manual recovery steps.

---

## EPIC-08 — Payment Service and Simulator

**Goal:** Model authorization, capture, refund, callbacks, and provider instability.

### FF-0801 — Bootstrap Payment Service

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0101, FF-0604

---

### FF-0802 — Implement payment domain model

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0801

**Acceptance criteria**

- Payment tracks order reference, provider reference, amount, currency, state, and version.
- Allowed state transitions are explicit.
- Raw card data is never accepted or stored.
- Stable idempotency reference is required for provider side effects.
- Monetary values use safe decimal representation.
- Domain tests cover all transitions.

---

### FF-0803 — Implement payment-provider simulator

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0002

**Supported behaviors**

- Success
- Decline
- Delayed response
- Timeout
- Duplicate callback
- Malformed callback
- Refund failure
- Temporary 5xx response

**Acceptance criteria**

- Behavior can be configured per request or scenario.
- Simulator exposes an inspection API for received requests.
- Duplicate callbacks reuse the same provider transaction.
- Simulator can introduce deterministic latency.
- Documentation includes example scenarios.

---

### FF-0804 — Implement authorize-payment command consumer

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0702, FF-0802, FF-0803

**Acceptance criteria**

- Duplicate commands do not create duplicate provider charges.
- Provider timeout produces a classified result, not an ambiguous success.
- Authorization result is published through outbox.
- Provider payload is mapped through an adapter.
- Integration tests cover success, decline, timeout, and duplicate command.

---

### FF-0805 — Implement capture and refund flows

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0804

**Acceptance criteria**

- Capture and refund use stable idempotency references.
- Invalid state transitions are rejected.
- Refund failures are retryable only when classified safe.
- Events are published for completed and failed operations.
- Audit record contains provider reference and normalized reason code.

---

### FF-0806 — Implement payment callback endpoint

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-0803, FF-0804

**Acceptance criteria**

- Callback signature is validated.
- Unknown transaction references are quarantined.
- Duplicate callbacks are safe.
- Out-of-order callbacks are handled or rejected predictably.
- Callback payload snapshots redact sensitive fields.
- Security and integration tests exist.

---

## EPIC-09 — Provisioning Service and Provider Migration

**Goal:** Demonstrate external integration, anti-corruption layers, and controlled provider migration.

### FF-0901 — Bootstrap Provisioning Service

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0101, FF-0604

---

### FF-0902 — Define internal provisioning domain

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0901

**Acceptance criteria**

- Internal domain does not expose provider-specific field names.
- Provisioning request has stable order and operation references.
- State machine covers requested, in progress, completed, failed, and cancelled.
- Provider response reasons are normalized.
- Domain tests cover transition rules.

---

### FF-0903 — Implement provider abstraction

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0902

```java
public interface ProvisioningProvider {
    ProvisioningResult provision(ProvisioningRequest request);
    ProvisioningStatus getStatus(String externalReference);
    CancellationResult cancel(String externalReference);
}
```

**Acceptance criteria**

- Domain layer depends on the interface, not provider implementations.
- Provider errors map to normalized categories.
- Each operation accepts a stable idempotency reference.
- Provider-specific DTOs stay inside adapter packages.

---

### FF-0904 — Implement legacy-provider simulator and adapter

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0903

**Acceptance criteria**

- Simulator supports synchronous success, asynchronous completion, timeout, rejection, and duplicate callback.
- Adapter maps snake_case legacy payloads to the internal model.
- Mapping tests cover every field and error code.
- Contract examples are documented.

---

### FF-0905 — Implement next-generation provider simulator and adapter

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0903

**Acceptance criteria**

- Simulator exposes a deliberately different contract.
- Adapter maps nested offering payloads to the internal model.
- Async status polling is supported.
- Mapping tests and provider contract tests exist.

---

### FF-0906 — Implement provisioning command consumer

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0702, FF-0904, FF-0905

**Acceptance criteria**

- Duplicate command is safe.
- Selected provider is persisted with the provisioning record.
- Result event is written through outbox.
- Retryability is based on normalized error category.
- Integration tests cover both providers.

---

### FF-0907 — Implement migration routing

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0906

**Routing modes**

- Customer segment
- Percentage
- Product allowlist
- Explicit operational override
- Feature flag

**Acceptance criteria**

- Routing decision is deterministic for percentage mode.
- Decision and rule version are audited.
- Configuration refresh behavior is documented.
- Invalid configuration fails safely.
- Unit tests cover boundary percentages and precedence.

---

### FF-0908 — Implement shadow comparison mode

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 8
- **Dependencies:** FF-0904, FF-0905, FF-0907

**Acceptance criteria**

- Only read-only operations can use shadow mode.
- Primary provider response is returned to the caller.
- Shadow failure cannot fail the primary operation.
- Responses are normalized before comparison.
- Differences are stored without sensitive data.
- Compatibility metrics are exposed.
- Report API returns match, mismatch, and technical-failure counts.

---

### FF-0909 — Implement provider operational override API

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0907, FF-0203

**Acceptance criteria**

- Only operations or admin role can change routing.
- Every change is audited.
- Override supports expiry.
- Invalid provider selection is rejected.
- Active configuration is queryable.

---

## EPIC-10 — Saga Orchestration

**Goal:** Coordinate the complete order workflow and compensation.

### FF-1001 — Design workflow command and event map

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0502, FF-0603

**Acceptance criteria**

- Every workflow step has command, success event, failure event, timeout behavior, and compensation.
- Diagram is stored under `docs/diagrams/`.
- Retryable and terminal errors are classified.
- ADR documents orchestration over choreography.

---

### FF-1002 — Implement successful order workflow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-0404, FF-0701, FF-0804, FF-0906, FF-1001

**Workflow**

```text
ValidateCustomer
ReserveCapacity
AuthorizePayment
ProvisionService
ActivateSubscription
CapturePayment
CompleteOrder
```

**Acceptance criteria**

- Order state advances only after the expected event.
- Events from previous steps cannot advance the workflow incorrectly.
- Duplicate events are ignored safely.
- State and outgoing command are persisted atomically through outbox.
- Correlation and causation IDs are preserved.
- End-to-end success test passes.

---

### FF-1003 — Implement provisioning-failure compensation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-1002, FF-0805

**Compensation**

```text
CancelProvisioning if required
Void or refund payment
ReleaseCapacity
MarkOrderFailed
```

**Acceptance criteria**

- Compensation executes in reverse side-effect order.
- Every compensation step is idempotent.
- Partial compensation failure leaves the order in a recoverable state.
- Failure reason and pending recovery action are visible in timeline.
- Integration tests cover successful and failed refund compensation.

---

### FF-1004 — Implement cancellation workflow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0505, FF-1002, FF-1003

**Acceptance criteria**

- Cancellation behavior varies by current workflow stage.
- No completed order is silently reverted.
- Races between completion and cancellation are deterministic.
- Customer receives final cancellation outcome.
- End-to-end concurrency test exists.

---

### FF-1005 — Implement workflow timeouts

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1002

**Acceptance criteria**

- Each pending step has a configurable deadline.
- Expired step generates a timeout transition exactly once.
- Timeout can trigger retry or compensation based on operation safety.
- Metrics expose timed-out steps by type.
- Clock behavior is testable without real waiting.

---

### FF-1006 — Implement workflow recovery after restart

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1002, FF-1005

**Acceptance criteria**

- In-progress orders resume after service restart.
- Recovery does not duplicate commands.
- Recovery query is indexed.
- Test terminates the service between state persistence and event handling.

---

## EPIC-11 — Notification and Dead-Letter Processing

### FF-1101 — Bootstrap Notification Service

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0002, FF-0604

---

### FF-1102 — Implement notification consumer

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-0702, FF-1101

**Acceptance criteria**

- Consumes completed, failed, and cancelled order events.
- Uses a mock email or webhook provider.
- Duplicate event does not create duplicate notification.
- Delivery attempts are stored.
- Sensitive customer data is masked.

---

### FF-1103 — Implement retry and dead-letter flow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1102

**Acceptance criteria**

- Retry count and backoff are configurable.
- Non-retryable failures go directly to DLQ.
- DLQ message preserves original envelope and failure metadata.
- Metrics expose retries and dead-letter count.
- Tests cover retry success and permanent failure.

---

### FF-1104 — Implement dead-letter inspection and replay API

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-1103, FF-0203

**Acceptance criteria**

- Operations role can list dead-letter records.
- Replay requires a reason.
- Replay creates an audit record.
- Same dead-letter item cannot be replayed concurrently.
- Replay does not bypass consumer idempotency.
- API supports pagination and filtering.

---

## EPIC-12 — Resilience and Reconciliation

### FF-1201 — Standardize HTTP client timeouts

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0803, FF-0904, FF-0905

**Acceptance criteria**

- Connection, response, and total operation timeouts are explicit.
- Defaults can be overridden per provider.
- Timeout events are distinguishable from provider rejection.
- Metrics expose timeout count by provider.

---

### FF-1202 — Add circuit breakers

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1201

**Acceptance criteria**

- Separate circuit breakers exist per provider operation.
- State transitions emit metrics and logs.
- Open circuit produces a normalized temporary-failure result.
- Recovery behavior is tested with deterministic provider failures.

---

### FF-1203 — Add bulkheads and concurrency limits

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-1201

**Acceptance criteria**

- Legacy and next-generation providers have independent concurrency pools or semaphores.
- Saturation fails quickly and predictably.
- Saturation metrics are exported.
- Load test proves one provider cannot consume all integration capacity.

---

### FF-1204 — Implement stuck-order reconciliation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1005, FF-1006

**Acceptance criteria**

- Job finds orders beyond state-specific age thresholds.
- Job classifies retry, compensate, or manual-review actions.
- Multiple replicas do not process the same order concurrently.
- Every action is audited.
- Metrics expose stuck orders by state and age bucket.

---

### FF-1205 — Implement payment-order reconciliation

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-0805, FF-1003

**Acceptance criteria**

- Detects captured or authorized payments without expected order state.
- Does not automatically refund ambiguous cases without policy.
- Produces a report and operational action.
- Test data covers orphaned authorization and orphaned capture.

---

### FF-1206 — Implement provisioning-subscription reconciliation

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-0906, FF-1002

**Acceptance criteria**

- Detects completed provisioning without active subscription.
- Detects active subscription without completed provisioning.
- Produces repair recommendation and audit record.
- Recovery operations remain idempotent.

---

## EPIC-13 — Observability

### FF-1301 — Add structured JSON logging

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002

**Required fields**

- timestamp
- level
- service
- environment
- traceId
- spanId
- correlationId
- orderId when applicable
- eventId when applicable
- eventType when applicable
- message

**Acceptance criteria**

- Production profile emits JSON.
- Development profile remains readable.
- Sensitive fields are masked.
- Exceptions include stack traces without leaking secrets.
- Logging conventions are documented.

---

### FF-1302 — Add OpenTelemetry instrumentation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0101, FF-1301

**Acceptance criteria**

- HTTP server and client spans are exported.
- Kafka producer and consumer spans are linked.
- Correlation ID appears as span attribute.
- Database spans are visible.
- One end-to-end order trace crosses all participating services.
- Trace sampling is configurable.

---

### FF-1303 — Add service metrics

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0101

**Technical metrics**

- Request rate
- Error rate
- Request duration
- JVM
- Connection pool
- Kafka producer errors
- Kafka consumer lag
- Outbox backlog

**Business metrics**

- Orders created
- Orders completed
- Orders failed
- Completion duration
- Payment declines
- Provisioning failures
- Compensation attempts
- Stuck orders
- Provider routing percentage
- Provider compatibility ratio

**Acceptance criteria**

- Metric names and labels are documented.
- High-cardinality identifiers are not labels.
- Prometheus can scrape all services.
- Metrics are covered by at least smoke tests.

---

### FF-1304 — Create Grafana dashboards

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1303

**Acceptance criteria**

- Dashboard provisioning is source controlled.
- Main dashboard shows throughput, success rate, P95 latency, workflow failures, consumer lag, and provider health.
- Migration dashboard shows provider traffic and compatibility.
- Panels link to relevant logs or traces where supported.
- Dashboard screenshots are stored in documentation.

---

### FF-1305 — Configure centralized logs

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0101, FF-1301

**Acceptance criteria**

- Logs are queryable by correlation ID and order ID.
- Retention is configurable.
- Local environment does not exhaust disk under normal demo use.
- Operations documentation contains common queries.

---

### FF-1306 — Define operational alerts

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-1303

**Minimum alerts**

- High order failure rate
- Oldest outbox event too old
- Kafka consumer lag above threshold
- Provider circuit open
- Stuck orders detected
- Reconciliation failures
- Dead-letter growth
- Database pool exhaustion

**Acceptance criteria**

- Alert definitions are source controlled.
- Each alert has severity, description, impact, and runbook link.
- Local test procedure exists.

---

## EPIC-14 — Security and Audit

### FF-1401 — Define service authorization matrix

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 2
- **Dependencies:** FF-0203

**Acceptance criteria**

- Matrix maps role to endpoint and operation.
- Customer ownership checks are explicit.
- Internal service-to-service permissions are documented.
- Deny-by-default is stated.

---

### FF-1402 — Implement service-to-service authentication

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0104, FF-1401

**Acceptance criteria**

- Internal APIs reject end-user tokens when service credentials are required.
- Client credentials use least-privilege scopes.
- Token acquisition is cached safely.
- Secret rotation procedure is documented.
- Integration tests cover valid, expired, wrong-audience, and insufficient-scope tokens.

---

### FF-1403 — Implement audit logging

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1401

**Audited operations**

- Customer suspension
- Manual order retry
- Dead-letter replay
- Provider routing override
- Migration configuration change
- Administrative recovery

**Acceptance criteria**

- Audit record includes actor, action, target, reason, previous value, new value, timestamp, and correlation ID.
- Audit records are append-only through the application.
- Sensitive fields are redacted.
- Audit queries are paginated.

---

### FF-1404 — Add dependency and container scanning

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0002

**Acceptance criteria**

- CI scans dependencies and images.
- Critical findings fail the pipeline unless a time-bounded exception is documented.
- Reports are retained as artifacts.
- Suppressions require reason, owner, and expiry.

---

### FF-1405 — Add security headers and input hardening

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-0201

**Acceptance criteria**

- Appropriate security headers are configured.
- Request size limits exist.
- Validation rejects unexpected enum and malformed identifier values.
- Error responses do not expose stack traces.
- Basic API security test suite exists.

---

## EPIC-15 — Test Strategy and Quality Gates

### FF-1501 — Create test-support library

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0002

**Acceptance criteria**

- Provides PostgreSQL, Kafka, Redis, and WireMock Testcontainers helpers.
- Avoids hidden global mutable state.
- Testcontainers are reusable where safe.
- Usage examples exist.
- Services can opt into only required dependencies.

---

### FF-1502 — Establish service integration-test conventions

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-1501

**Acceptance criteria**

- PostgreSQL integration tests do not use H2.
- Tests apply real Flyway migrations.
- Tests run against real Kafka where event behavior matters.
- Test naming and package conventions are documented.
- CI executes integration tests separately from unit tests.

---

### FF-1503 — Add contract tests

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-0603, FF-0804, FF-0906

**Required contracts**

- Order to Payment
- Order to Provisioning
- Provisioning to legacy provider
- Provisioning to next-generation provider
- Next.js BFF to Gateway
- Gateway to public APIs

**Acceptance criteria**

- Provider and consumer verification are automated.
- Contract artifacts are versioned.
- Breaking changes fail CI.
- Contract compatibility policy is documented.

---

### FF-1504 — Build end-to-end test harness

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-1003, FF-1103, FF-1302

**Required scenarios**

1. Successful order
2. Payment declined
3. Payment timeout
4. Provisioning failure and successful refund
5. Refund failure requiring reconciliation
6. Duplicate order request
7. Duplicate Kafka event
8. Duplicate provider callback
9. Legacy-provider outage
10. Next-generation provider routing
11. Dead-letter replay
12. Concurrent cancellation and completion
13. Service restart during workflow
14. Outbox recovery after Kafka outage

**Acceptance criteria**

- Tests run from one command.
- Test data is isolated.
- Failed scenarios retain browser screenshots, video or trace files, backend logs, and distributed traces as artifacts.
- Scenarios are deterministic.

---

### FF-1505 — Define code-coverage policy

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 2
- **Dependencies:** FF-0002

**Acceptance criteria**

- Coverage is measured per module.
- Domain and application packages have higher expectations than configuration code.
- Coverage thresholds prevent regressions without encouraging meaningless tests.
- Exclusions are documented.

---

### FF-1506 — Add mutation testing for critical domain logic

- **Status:** DRAFT
- **Priority:** P3
- **Estimate:** 5
- **Dependencies:** FF-0502, FF-0802, FF-0902

**Acceptance criteria**

- Mutation testing covers order, payment, and provisioning state machines.
- Surviving mutations are reviewed.
- CI may run mutation tests on schedule rather than every pull request.

---

## EPIC-16 — Kubernetes and Helm

### FF-1601 — Create base Kubernetes manifests

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0101

**Acceptance criteria**

- Namespace, Deployments, Services, ConfigMaps, Secret templates, and Ingress exist for backend services and the Next.js web application.
- Resource requests and limits are set.
- Liveness, readiness, and startup probes are configured.
- No plaintext production secrets are committed.
- Manifests pass validation.

---

### FF-1602 — Package services with Helm

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-1601

**Acceptance criteria**

- Shared chart patterns avoid copy-paste without hiding service differences.
- Values exist for local, development, and demo environments.
- Image repository and tag are configurable.
- `helm lint` and template validation run in CI.
- Upgrade and rollback commands are documented.

---

### FF-1603 — Add autoscaling and disruption controls

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-1602, FF-1303

**Acceptance criteria**

- HorizontalPodAutoscaler exists for stateless services.
- PodDisruptionBudget exists where multiple replicas are expected.
- Consumer scaling limitations and partition count are documented.
- Graceful shutdown permits in-flight message handling to complete or recover.

---

### FF-1604 — Add network policies

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-1602

**Acceptance criteria**

- Default-deny ingress policy exists.
- Services may access only required dependencies.
- The public ingress exposes the Next.js web application; backend gateway access is restricted to approved paths or internal callers according to the environment design.
- Policies are documented and tested in a compatible cluster.

---

### FF-1605 — Demonstrate rolling-update safety

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1602, FF-1006

**Acceptance criteria**

- Rolling deployment of either the Next.js runtime or backend services does not lose accepted orders.
- Readiness prevents traffic to unready pods.
- Kafka consumers stop gracefully.
- Test evidence includes order completion during a rolling update.

---

## EPIC-17 — CI/CD

### FF-1701 — Create pull-request CI workflow

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 5
- **Dependencies:** FF-0002

**Stages**

```text
backend-compile
web-typecheck
unit-test
component-test
integration-test
static-analysis
dependency-scan
```

**Acceptance criteria**

- Workflow uses dependency caching safely.
- Failed stage blocks merge.
- Test and coverage reports are retained.
- Concurrent outdated workflow runs are cancelled.
- Branch protection requirements are documented.

---

### FF-1702 — Create container-build workflow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1701, FF-1404

**Acceptance criteria**

- Backend and Next.js images use reproducible multi-stage Dockerfiles.
- Images run as non-root.
- Image tags include immutable commit SHA.
- SBOM is generated.
- Container scan runs before publication.
- Build provenance is retained where supported.

---

### FF-1703 — Create development deployment workflow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1602, FF-1702

**Acceptance criteria**

- Deployment uses immutable image tags.
- Database migration behavior is explicit.
- Smoke tests run after deployment.
- Failure triggers clear rollback instructions.
- Environment protection rules are documented.

---

### FF-1704 — Add release workflow

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 3
- **Dependencies:** FF-1703

**Acceptance criteria**

- Semantic version tags create a release.
- Changelog entries are generated or validated.
- Release includes architecture, API, event, and deployment artifacts.
- Release notes include migrations and known limitations.

---

## EPIC-18 — Performance and SQL Optimization

### FF-1801 — Create k6 order workload

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1002, FF-1303

**Acceptance criteria**

- Workload supports configurable users, duration, and arrival rate.
- Test data does not reuse conflicting idempotency keys.
- Output captures P50, P95, P99, throughput, and error rate.
- Results include environment details.
- Initial targets are clearly labeled as targets, not achieved claims.

---

### FF-1802 — Test provider slowdown behavior

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1202, FF-1203, FF-1801

**Acceptance criteria**

- Test injects provider latency and failures.
- Results show circuit, bulkhead, queue, and consumer-lag behavior.
- System remains responsive for unaffected operations.
- Findings and bottlenecks are documented.

---

### FF-1803 — Document SQL optimization case study

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0504, FF-1006

**Acceptance criteria**

- Select one meaningful query such as stuck-order lookup or timeline retrieval.
- Capture original SQL and `EXPLAIN ANALYZE`.
- Apply index or query redesign.
- Capture before-and-after execution statistics.
- Explain write and storage trade-offs.
- Use representative generated data.

---

### FF-1804 — Validate restart and recovery under load

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1006, FF-1605, FF-1801

**Acceptance criteria**

- Restart at least one order-service and one consumer replica during load.
- No accepted order is silently lost.
- Duplicate processing remains safe.
- Recovery duration and backlog drain are measured.
- Results are published in performance documentation.

---

## EPIC-19 — Migration Showcase

### FF-1901 — Create migration compatibility report API

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0908

**Acceptance criteria**

- Report groups results by operation, product, provider version, and time period.
- API returns total, matched, mismatched, technical failures, and ratio.
- Pagination is applied to mismatch details.
- Sensitive payloads are redacted.

---

### FF-1902 — Build migration dashboard

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-1304, FF-1901

**Acceptance criteria**

- Shows traffic split, compatibility ratio, mismatch categories, and provider errors.
- Supports filtering by product and operation.
- Dashboard JSON is source controlled.

---

### FF-1903 — Document API gap-analysis example

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0904, FF-0905

**Acceptance criteria**

- Compares legacy and next-generation contracts field by field.
- Lists missing, renamed, transformed, optional, and incompatible fields.
- Documents mitigation in the anti-corruption layer.
- Includes impact assessment for downstream workflows.
- Does not contain confidential real-company information.

---

### FF-1904 — Demonstrate controlled cutover and rollback

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0907, FF-0909, FF-1902

**Acceptance criteria**

- Demo changes next-generation traffic from 0% to 20%.
- Routing is observable in metrics.
- Forced provider failure triggers documented operational response.
- Override restores safe routing.
- Audit trail captures all changes.

---

## EPIC-20 — Next.js Web Foundation

**Goal:** Establish a production-ready Next.js application and frontend engineering conventions.

**Exit criteria:**

- Web application builds from a clean checkout
- Public, customer, and operations route groups exist
- Component and feature boundaries are documented
- The application has consistent loading, error, empty, and unauthorized states
- Accessibility and responsive-design baselines are enforced

### FF-2001 — Initialize Next.js application

- **Status:** IN_REVIEW
- **Priority:** P0
- **Estimate:** 3
- **Dependencies:** FF-0001
- **Owner:** Unassigned

**Scope**

Create `apps/web` using the Next.js App Router, TypeScript strict mode, pnpm, ESLint, and production build scripts.

**Acceptance criteria**

- `pnpm --dir apps/web build` succeeds.
- App Router is used; no Pages Router files are introduced.
- TypeScript strict mode is enabled.
- `next`, `react`, and `react-dom` versions are pinned through the lockfile.
- Root workspace scripts can lint, type-check, test, build, and start the web app.
- A production health endpoint is available without exposing secrets.
- Source maps and build output behavior are documented.

**Required tests/evidence**

- Clean installation with `pnpm install --frozen-lockfile`
- Production build log
- Health endpoint smoke test

**Agent notes**

Do not add feature-specific pages in this ticket.

---

### FF-2002 — Define frontend architecture and route groups

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-2001

**Route groups**

```text
(public)       landing, sign-in, access-denied
(customer)     dashboard, catalog, checkout, orders, subscriptions
(operations)   operational dashboard, order search, DLQ, reconciliation, migration
```

**Acceptance criteria**

- Route ownership and intended roles are documented.
- Each route group has a dedicated layout.
- Server Components are the default.
- Client Components are isolated behind explicit interaction boundaries.
- Feature modules do not import from other feature internals.
- Architecture document explains data-fetching and mutation patterns.
- Deep links preserve the intended post-login destination.

---

### FF-2003 — Create design-system foundation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2001

**Minimum components**

- Button
- Link
- Text input
- Select
- Checkbox
- Form field and validation message
- Alert
- Toast
- Modal or dialog
- Table
- Pagination
- Tabs
- Badge
- Skeleton
- Empty state
- Error state

**Acceptance criteria**

- Components support keyboard navigation and visible focus.
- Semantic HTML is used before ARIA.
- Form controls have programmatic labels and error associations.
- Components support light and dark-safe tokens even if only one theme ships initially.
- Responsive behavior is demonstrated at mobile, tablet, and desktop widths.
- Component examples exist in a development-only catalog page or Storybook if later approved.
- No application-specific business logic exists in base UI components.

---

### FF-2004 — Define web configuration and environment validation

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 3
- **Dependencies:** FF-2001

**Acceptance criteria**

- Server-only and browser-safe variables are separated.
- Required environment variables are validated at startup.
- Secrets cannot be exported through `NEXT_PUBLIC_*`.
- Backend gateway URL, Keycloak issuer, and application base URL are configurable.
- Invalid configuration fails startup with a non-secret diagnostic.
- `.env.example` documents local values without real credentials.

---

### FF-2005 — Implement global loading and error handling

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-2002, FF-2003

**Acceptance criteria**

- Root and route-level loading states exist.
- Expected domain errors render actionable messages.
- Unexpected errors render a safe fallback and correlation ID.
- Unauthorized and forbidden states are distinct.
- Retry controls do not repeat unsafe mutations.
- Global error handling emits structured telemetry.
- Component tests cover rendering and retry behavior.

---

### FF-2006 — Add responsive application shell

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2002, FF-2003

**Acceptance criteria**

- Customer and operations navigation adapt to small screens.
- Current route is indicated accessibly.
- Skip link and landmark structure are present.
- User menu exposes role and sign-out.
- Navigation items derive from authorized capabilities, not only hidden CSS.
- Layout does not introduce horizontal scrolling at supported widths.
- Keyboard-only navigation is tested manually and through Playwright.

---

## EPIC-21 — Web Authentication and Backend-for-Frontend Integration

**Goal:** Securely connect the Next.js runtime to Keycloak and the Spring Cloud Gateway.

### FF-2101 — Integrate Keycloak OIDC session

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 8
- **Dependencies:** FF-0104, FF-2001

**Scope**

Configure Auth.js with Keycloak and a server-managed session.

**Acceptance criteria**

- Sign-in redirects to Keycloak and returns to the requested application route.
- Sign-out clears the application session and performs the documented provider logout behavior.
- Cookies use `HttpOnly`, `Secure` in non-local environments, and an appropriate `SameSite` policy.
- Access and refresh tokens are never exposed in page props, client components, logs, or browser storage.
- Session contains only the minimum user identity and role information needed by the UI.
- Expired or revoked sessions redirect safely to sign-in.
- Authentication callbacks validate issuer, state, and nonce according to the chosen library behavior.
- Integration tests cover successful login, logout, expired session, and invalid role mapping.

**Security review required:** Yes

---

### FF-2102 — Implement role and capability authorization

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2101, FF-1401

**Acceptance criteria**

- Route access maps to `CUSTOMER`, `SUPPORT_AGENT`, `OPERATIONS`, and `ADMIN` capabilities.
- Authorization runs on the server before protected content is rendered.
- Client-side hiding is not treated as an authorization control.
- A user with multiple roles receives the union of allowed capabilities.
- Access-denied page does not reveal protected data.
- Tests cover every route group and allow/deny combination.

---

### FF-2103 — Create typed Gateway API client

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2001, FF-0303, FF-0403, FF-0503, FF-0504

**Acceptance criteria**

- TypeScript types and client functions are generated from versioned OpenAPI documents.
- Generated source is reproducible through one command.
- Generated files are not manually edited.
- Build fails when generated contracts are stale.
- Client supports correlation ID, bearer token, timeout, and normalized error mapping.
- Non-2xx responses preserve stable backend error codes.
- Contract generation does not expose internal-only APIs to browser bundles.

---

### FF-2104 — Implement BFF request wrapper

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2101, FF-2103, FF-0202

**Acceptance criteria**

- Server Components, Route Handlers, and server-side mutations use one request wrapper.
- Wrapper obtains the server-side access token without exposing it to the browser.
- Browser correlation ID is accepted when valid or generated server-side.
- The same correlation ID is returned to the browser and forwarded to the gateway.
- Timeout and cancellation behavior are explicit.
- Backend 401 causes safe session recovery or reauthentication.
- Backend 403 is not converted to 404 unless a documented privacy rule requires it.
- Logs include route, method, status, duration, and correlation ID without sensitive payloads.

---

### FF-2105 — Add same-origin Route Handlers for browser mutations

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2104

**Acceptance criteria**

- Browser mutations call only same-origin Next.js routes.
- Route Handlers validate session, content type, payload size, and Zod schema.
- Backend remains authoritative for domain validation.
- Route Handlers preserve idempotency keys for order and retry operations.
- Sensitive backend response fields are not forwarded unnecessarily.
- CSRF protections match the session and mutation architecture.
- Integration tests cover unauthorized, validation, timeout, backend error, and success paths.

---

### FF-2106 — Implement session expiry and token-refresh user experience

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-2101, FF-2104

**Acceptance criteria**

- Recoverable token expiry refreshes without losing safe page state.
- Irrecoverable expiry redirects to sign-in with a non-sensitive return URL.
- Unsaved form behavior is documented.
- Multiple simultaneous requests do not cause uncontrolled refresh storms.
- Refresh failures are observable without logging tokens.

---

### FF-2107 — Add web security headers and content policy

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2001, FF-2101, FF-1405

**Acceptance criteria**

- Content Security Policy is documented and enforced without broad unsafe exceptions.
- Frame, referrer, MIME-sniffing, permissions, and transport headers are configured appropriately.
- Inline-script requirements from Next.js or authentication are handled through a reviewed strategy.
- Security headers are verified in integration tests.
- Production error pages do not leak configuration or stack traces.

---

## EPIC-22 — Customer Portal

**Goal:** Deliver a complete customer journey from product discovery to subscription management.

### FF-2201 — Build customer dashboard

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2006, FF-2104, FF-0303, FF-0504

**Acceptance criteria**

- Dashboard shows customer profile summary, active subscriptions, and recent orders.
- Data is rendered server-side on initial request.
- Independent sections fail independently where possible.
- Empty, loading, partial-error, and unauthorized states are implemented.
- Customer can navigate to order and subscription details.
- No customer can access another customer's data by URL manipulation.

---

### FF-2202 — Build product catalog and product detail pages

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2006, FF-2103, FF-0403

**Acceptance criteria**

- Catalog supports pagination and documented filters.
- Product detail shows effective price, features, add-ons, and eligibility notes.
- Filter state is represented in the URL.
- Server rendering supports deep links and refresh.
- Stale or unavailable product versions show a clear outcome.
- Cards and controls pass keyboard and accessible-name checks.

---

### FF-2203 — Build subscription checkout flow

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-2202, FF-2105, FF-0503

**Flow**

```text
Select product
Select add-ons
Choose installation address
Choose fictional payment token
Review order
Submit
```

**Acceptance criteria**

- Checkout preserves state between steps without exposing tokens or sensitive data.
- Form validation uses Zod and accessible field errors.
- Final submission generates one idempotency key and reuses it for safe retries.
- Submit button prevents accidental duplicate interaction without relying on the button alone for idempotency.
- Backend validation errors map to relevant steps or a summary.
- Successful submission redirects to the order detail page.
- Browser refresh after submission does not create another order.
- Playwright covers successful and rejected checkout.

---

### FF-2204 — Build live order detail and timeline

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2203, FF-0504, FF-1002

**Acceptance criteria**

- Initial order and timeline are server-rendered.
- Non-terminal orders refresh through bounded client-side polling.
- Polling stops when the page is hidden for a documented interval and resumes safely.
- Terminal states stop polling.
- Timeline visually distinguishes completed, current, compensated, and failed steps.
- Failure messages are customer-safe and include support correlation ID.
- Status changes are announced accessibly without excessive screen-reader noise.

---

### FF-2205 — Build order cancellation experience

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2204, FF-0505, FF-2105

**Acceptance criteria**

- Cancellation action appears only for potentially cancellable states, while backend authorization remains authoritative.
- Confirmation explains expected compensation behavior.
- Request uses a stable idempotency key.
- Pending cancellation prevents duplicate actions.
- Conflicting completion response is shown clearly.
- Timeline updates to final cancellation or failure state.
- Playwright covers cancellation and completion race behavior.

---

### FF-2206 — Build subscription detail page

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-2201, FF-1002

**Acceptance criteria**

- Page shows product, status, activation time, provider-safe reference, and related order.
- Only customer-safe operational details are displayed.
- Missing or terminated subscriptions have explicit states.
- Deep links enforce ownership.

---

### FF-2207 — Add customer notification center

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-1102, FF-2201

**Acceptance criteria**

- Customer can view recent order-related notification records.
- Read state is local to the application or backed by an explicitly owned API.
- Notification content contains no secrets.
- Empty and delivery-failure states are represented.

---

## EPIC-23 — Operations Console

**Goal:** Provide an auditable operational interface for investigation and recovery.

### FF-2301 — Build operations dashboard

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2006, FF-2102, FF-1303

**Acceptance criteria**

- Shows order throughput, success rate, failures by stage, stuck orders, DLQ count, and provider health.
- Dashboard makes metric freshness visible.
- Server-side summary data is used where an operations API exists; direct browser access to Prometheus is prohibited.
- Charts have accessible text summaries and table alternatives for critical values.
- Empty and degraded-data states are explicit.

---

### FF-2302 — Build order search and investigation view

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0504, FF-2104, FF-2102

**Acceptance criteria**

- Search supports order ID, customer reference, status, provider, and time range.
- Results are paginated and URL-addressable.
- Detail view shows timeline, normalized failures, correlation ID, payment summary, provisioning summary, and compensation state.
- Sensitive payment and identity data are redacted.
- Support agents have read-only capabilities; operations actions require stronger roles.

---

### FF-2303 — Build stuck-order recovery interface

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1204, FF-0506, FF-1403, FF-2302

**Acceptance criteria**

- Lists stuck orders with state, age, reason, and recommended action.
- Retry or compensate controls require a reason and confirmation.
- UI never offers an action the backend declares invalid.
- Concurrent action conflicts return a clear refreshed state.
- Audit actor and reason are shown after completion.
- Playwright covers authorized recovery and denied access.

---

### FF-2304 — Build dead-letter inspection and replay interface

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1104, FF-2105, FF-1403

**Acceptance criteria**

- List supports topic, event type, failure class, and date filters.
- Payload rendering redacts protected values and handles invalid JSON safely.
- Replay requires reason and confirmation.
- Replay status and audit link are visible.
- Double-click or refresh cannot trigger duplicate replay requests.

---

### FF-2305 — Build migration routing controls

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-0909, FF-2105, FF-1403

**Acceptance criteria**

- Displays active routing mode, percentage, products, override, version, and expiry.
- Editing uses optimistic concurrency or configuration version checks.
- Destructive routing changes require explicit confirmation.
- UI previews the resulting rule before submission.
- Successful change displays audit reference.
- Invalid and stale configuration responses are recoverable.

---

### FF-2306 — Build migration compatibility report

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1901, FF-1902, FF-2104

**Acceptance criteria**

- Report shows traffic split, match rate, mismatches, and technical failures.
- Filters are reflected in the URL.
- Mismatch details are paginated and redact protected data.
- Charts include accessible summaries.
- Data freshness and comparison window are visible.

---

### FF-2307 — Build audit-log viewer

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-1403, FF-2104

**Acceptance criteria**

- Viewer supports actor, action, target, and time filters.
- Previous and new values are rendered safely.
- Audit records are read-only.
- Role restrictions are tested.
- Export, if implemented, applies the same redaction and authorization rules.

---

## EPIC-24 — Frontend Quality, Accessibility, and Observability

### FF-2401 — Establish frontend unit and component tests

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2001, FF-2003

**Acceptance criteria**

- Vitest, React Testing Library, and user-event are configured.
- Tests focus on behavior and accessibility rather than implementation details.
- Server-only modules are not accidentally bundled into browser tests.
- Coverage reports are generated.
- Critical form, table, error, and authorization components are covered.

---

### FF-2402 — Establish Playwright full-stack browser tests

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 8
- **Dependencies:** FF-1504, FF-2205, FF-2305

**Required journeys**

1. Customer login and dashboard
2. Product discovery and checkout
3. Live order completion
4. Provisioning failure and compensation
5. Duplicate checkout submission
6. Customer cancellation
7. Support order investigation
8. Operations stuck-order recovery
9. Dead-letter replay
10. Provider migration change
11. Unauthorized role denial
12. Session expiry recovery

**Acceptance criteria**

- Tests run against production builds.
- Keycloak login uses dedicated test users and isolated state.
- Backend scenario simulators provide deterministic outcomes.
- Failed tests retain screenshot, trace, and video artifacts according to CI policy.
- Tests avoid arbitrary sleep and use observable conditions.

---

### FF-2403 — Add automated accessibility checks

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2003, FF-2402

**Acceptance criteria**

- axe checks run on major routes and dialogs.
- No known critical or serious automated violations remain.
- Manual checklist covers keyboard-only use, focus order, zoom, reduced motion, landmarks, headings, and representative screen-reader paths.
- Color is not the only means of conveying status.
- Accessibility exceptions require issue, owner, rationale, and target date.

---

### FF-2404 — Add frontend telemetry and correlation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1302, FF-2005, FF-2104

**Acceptance criteria**

- Server-side Next.js requests emit traces linked to gateway traces.
- Browser navigation and web-vital telemetry are collected without sensitive payloads.
- Frontend errors include correlation ID and route.
- User identity is not placed in high-cardinality metric labels.
- Source map access is restricted appropriately.
- One customer order can be followed from browser interaction through backend traces.

---

### FF-2405 — Define web performance budgets

- **Status:** DRAFT
- **Priority:** P2
- **Estimate:** 5
- **Dependencies:** FF-2204, FF-2301

**Acceptance criteria**

- Budgets cover JavaScript transferred, route load, Core Web Vitals targets, image size, and critical request count.
- Lighthouse-based checks run on representative production routes.
- Customer checkout and order detail are tested on a documented mobile profile.
- Performance claims include environment and test method.
- Regressions above agreed tolerance fail CI or create a tracked exception.

---

### FF-2406 — Add frontend visual regression checks

- **Status:** DRAFT
- **Priority:** P3
- **Estimate:** 5
- **Dependencies:** FF-2003, FF-2402

**Acceptance criteria**

- Stable representative states have screenshots.
- Dynamic timestamps and identifiers are masked.
- Baseline updates require explicit review.
- Mobile and desktop layouts are represented.

---

## EPIC-25 — Web Deployment and Delivery

### FF-2501 — Create production Next.js Docker image

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2001, FF-2004

**Acceptance criteria**

- Multi-stage build uses frozen pnpm dependencies.
- Production image uses Next.js standalone output or another documented minimal runtime strategy.
- Container runs as non-root.
- Only required runtime files are copied.
- Health check is available.
- Build-time and runtime configuration boundaries are documented.
- Image scan has no unresolved critical finding.

---

### FF-2502 — Add web application to Docker Compose

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-0101, FF-2501, FF-2101

**Acceptance criteria**

- Full platform starts with one documented command.
- Web container waits on required healthy dependencies without an infinite startup loop.
- Keycloak callback URLs work in local Compose.
- Backend gateway is reachable only through the documented network path.
- Hot-reload development remains available outside the production container workflow.

---

### FF-2503 — Add Next.js Kubernetes and Helm configuration

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1602, FF-2501

**Acceptance criteria**

- Deployment, Service, ConfigMap, Secret references, probes, resources, and ingress are templated.
- Public ingress routes application traffic to Next.js.
- Authentication callback and trusted host configuration work behind ingress.
- Backend gateway exposure follows the environment security design.
- Rolling updates preserve active sessions according to the chosen session strategy.
- Horizontal scaling behavior is documented, including cache implications.

---

### FF-2504 — Add frontend CI workflow

- **Status:** DRAFT
- **Priority:** P0
- **Estimate:** 5
- **Dependencies:** FF-1701, FF-2001, FF-2401

**Stages**

```text
pnpm-install-frozen
lint
typecheck
unit-component-test
build
contract-drift-check
```

**Acceptance criteria**

- pnpm cache is keyed safely by lockfile.
- Lockfile changes are reviewed.
- Generated API client drift fails CI.
- Build warnings requiring action are not hidden.
- Test and coverage artifacts are retained.
- Java and web checks can run in parallel where dependencies allow.

---

### FF-2505 — Add browser-test deployment pipeline

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1703, FF-2402, FF-2503

**Acceptance criteria**

- Ephemeral or dedicated test environment deploys exact commit images.
- Required backend scenarios and users are seeded reproducibly.
- Playwright smoke suite runs after deployment.
- Failure blocks promotion and preserves artifacts.
- Environment cleanup is automated and safe.

---

### FF-2506 — Validate web rolling updates and session behavior

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-2503, FF-2106

**Acceptance criteria**

- Active authenticated users can continue or recover predictably during a rolling update.
- In-flight safe reads may retry; unsafe mutations do not repeat without idempotency.
- Old and new web versions remain API-compatible during the deployment window.
- Test evidence includes checkout or order viewing during rollout.

---

## EPIC-26 — Documentation and Portfolio Release

### FF-2601 — Write root README

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** M5 complete

**Required sections**

- Problem statement
- Architecture overview
- Technology stack
- Key engineering decisions
- Local setup
- Customer and operations user journeys
- Web screenshots and example API calls
- Failure scenarios
- Observability
- Security
- Testing
- Deployment
- Performance results
- Limitations
- Roadmap

**Acceptance criteria**

- New developer can start the web application and backend platform using documented commands.
- README links detailed documents instead of becoming unmaintainable.
- Claims are supported by evidence.
- Diagrams are legible.

---

### FF-2602 — Write architecture documentation

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** M5 complete

**Acceptance criteria**

- System context, browser/BFF/backend container, route, and workflow diagrams exist.
- Service ownership and data boundaries are explicit.
- Sync and async interactions are documented.
- Failure and compensation paths are included.
- Architectural trade-offs are discussed.

---

### FF-2603 — Write operations runbook

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** FF-1204, FF-1306, FF-1104

**Required runbooks**

- Kafka outage
- Stuck order
- Provider circuit open
- Refund failure
- Dead-letter growth
- Outbox backlog
- Database connectivity issue
- Routing rollback
- Reconciliation failure

**Acceptance criteria**

- Each runbook includes symptoms, checks, mitigation, recovery, and escalation.
- Commands are safe and environment-aware.
- Destructive operations are clearly marked.

---

### FF-2604 — Create five-minute demo script

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 3
- **Dependencies:** FF-1904, FF-1504

**Required demo paths**

1. Successful order
2. Provisioning failure and compensation
3. Duplicate request and event safety
4. Provider migration routing
5. Trace, metrics, and logs

**Acceptance criteria**

- Script includes commands and expected output.
- Demo can be reset to a known state.
- Every claim shown has evidence.
- Total target duration is five to eight minutes.

---

### FF-2605 — Publish release candidate

- **Status:** DRAFT
- **Priority:** P1
- **Estimate:** 5
- **Dependencies:** All P0 and P1 items

**Acceptance criteria**

- Clean-clone setup is verified.
- CI and release workflows pass.
- Major routes pass automated accessibility checks and documented manual keyboard review.
- Responsive behavior is verified at supported mobile and desktop widths.
- Web performance measurements are published with the test environment.
- Known limitations are documented.
- Security scans have no unresolved critical findings.
- Demo recording and deployed web application are linked when public hosting is available.
- Performance report contains measured values.
- Resume-ready project description is finalized.

---

# 9. Codex Agent Development Workflow

## 9.1 Before Starting Work

A coding agent must:

1. Read `AGENTS.md`.
2. Read this backlog entry and every dependency.
3. Inspect relevant ADRs, API contracts, generated web clients, event schemas, migrations, route ownership, accessibility expectations, and neighboring tests.
4. Verify that the item status is `READY`.
5. Confirm that no other active change overlaps the same files, UI route, generated contract, database migration, or event contract.
6. Restate the ticket scope in its working notes.
7. Identify the smallest valid vertical slice, including frontend and backend changes only when both are required by the ticket.
8. Avoid modifying files outside the ticket unless required for correctness.

If the ticket is not `READY`, the agent may improve the backlog specification but must not begin product implementation.

---

## 9.2 Branch and Commit Rules

Branch format:

```text
feature/FF-####-short-description
fix/FF-####-short-description
chore/FF-####-short-description
```

Commit format:

```text
feat(order): FF-0503 require idempotency key
fix(payment): FF-0804 prevent duplicate authorization
test(provisioning): FF-0906 cover provider timeout
docs(adr): FF-1001 document saga orchestration
```

Rules:

- Keep commits logically cohesive.
- Do not combine formatting-only changes with behavior changes.
- Do not rewrite unrelated history.
- Do not force-push shared branches.
- Do not commit generated secrets, local databases, or build outputs.

---

## 9.3 Agent Implementation Loop

For each ticket:

1. Mark the item `IN_PROGRESS`.
2. Add or update the smallest appropriate tests: unit, component, integration, contract, accessibility, or browser tests.
3. Implement the smallest code change that satisfies the tests.
4. Run focused tests.
5. Run module-level checks.
6. Run root-level checks if shared code changed.
7. Update API, generated client, route, UX state, event, schema, ADR, accessibility, or operations documentation when affected.
8. Review the diff for unrelated changes.
9. Prepare a pull-request summary.
10. Mark the item `IN_REVIEW`.

Agents must not mark an item `DONE`; completion requires merge and human or CI confirmation.

---

## 9.4 Required Validation Commands

Use repository-provided commands. Expected baseline:

```bash
./gradlew spotlessCheck
./gradlew check
./gradlew test
./gradlew integrationTest
pnpm install --frozen-lockfile
pnpm --dir apps/web lint
pnpm --dir apps/web typecheck
pnpm --dir apps/web test
pnpm --dir apps/web build
docker compose config
helm lint platform/helm/flowforge
```

Run only relevant subsets during development, but the final pull request must state which commands were run and which were not.

For service changes:

```bash
./gradlew :services:<service-name>:test
./gradlew :services:<service-name>:integrationTest
```

For API or event changes:

```bash
./gradlew contractTest
pnpm --dir apps/web api:generate
pnpm --dir apps/web api:check
```

For frontend changes:

```bash
pnpm --dir apps/web lint
pnpm --dir apps/web typecheck
pnpm --dir apps/web test
pnpm --dir apps/web build
```

For browser journeys:

```bash
pnpm --dir apps/web test:e2e
```

For Docker changes:

```bash
docker build <target>
docker run --rm <target> --help
```

For Kubernetes changes:

```bash
helm template ...
kubectl apply --dry-run=client ...
```

Do not claim a command passed unless it was actually executed.

---

## 9.5 Agent Stop Conditions

Stop implementation and request human review when:

- A public API, event, generated frontend contract, route contract, or authentication flow requires a breaking change.
- A core technology decision must change.
- A database migration could lose or rewrite existing data.
- Security requirements conflict with requested behavior.
- Acceptance criteria contradict existing contracts.
- A dependency is not complete.
- Required credentials or external systems are unavailable.
- A test exposes an architectural defect outside ticket scope.
- More than three unrelated backend modules or frontend feature areas require modification.
- The proposed change would add domain logic to a shared library.
- The item exceeds the expected 8-point scope.
- A production-like secret appears in source control.
- A change would expose access or refresh tokens to browser JavaScript.
- Required interaction, error, or accessibility behavior is not specified and cannot be inferred safely from existing patterns.

The agent should document the blocker and recommend the smallest next decision.

---

## 9.6 Prohibited Agent Actions

Agents must not:

- Disable failing tests to obtain a green build.
- Reduce quality gates without an approved ticket.
- Invent performance results.
- Commit secrets.
- Add unreviewed external dependencies.
- Change public contracts silently.
- Use another service's database.
- add automatic retries to unsafe side effects without idempotency.
- Log access tokens, payment tokens, or sensitive personal data.
- Add broad catch blocks that hide failures.
- Add global mutable state for tests.
- Store access or refresh tokens in `localStorage`, `sessionStorage`, or browser-readable cookies.
- Call individual microservices directly from browser code.
- Handwrite frontend API DTOs when generated contracts exist.
- Convert Server Components to Client Components only to simplify data fetching.
- Suppress TypeScript, ESLint, hydration, or accessibility errors without documented justification.
- Replace PostgreSQL integration tests with H2.
- Create a large generic shared domain module.
- Perform unrelated refactors.
- Mark incomplete work as done.

---

## 9.7 Pull-Request Handoff Template

```markdown
## Backlog

- Ticket: FF-####
- Status: IN_REVIEW

## Summary

Describe the behavior implemented and why.

## Scope

- Included:
- Excluded:

## Architecture Impact

- Frontend routes and UX states:
- API and generated client:
- Events:
- Database:
- Security and session behavior:
- Accessibility:
- Observability:
- Deployment:

## Validation

- [ ] Backend unit tests
- [ ] Frontend unit/component tests
- [ ] Integration tests
- [ ] Contract and generated-client checks
- [ ] Playwright end-to-end tests
- [ ] Accessibility checks
- [ ] Static analysis and type-checking
- [ ] Backend and web container builds
- [ ] Helm validation

Commands executed:

```text
paste exact commands
```

## Evidence

- Test results:
- Example request/response:
- Logs, metrics, or traces:
- Screenshots or artifacts:

## Risk

Describe failure modes and affected components.

## Migration and Rollback

Describe database migration, compatibility, deployment order, and rollback.

## Follow-up

List explicitly deferred work and related backlog IDs.
```

---

# 10. Definition of Ready

A backlog item is `READY` only when:

- Goal and business value are clear.
- Scope and exclusions are explicit.
- Dependencies are complete or scheduled.
- Acceptance criteria are testable.
- Required API and event contracts are available.
- Data ownership is known.
- Security, session, role, ownership, and authorization impact is identified.
- Observability expectations are identified.
- Customer-visible loading, empty, success, failure, timeout, and retry states are identified.
- Responsive and accessibility behavior is specified for frontend work.
- OpenAPI generation impact is identified when a backend contract changes.
- Migration and backward-compatibility needs are known.
- Estimate is 8 points or less.
- No unresolved architecture decision blocks implementation.
- Test environment and required simulators are available.

---

# 11. Definition of Done

A backlog item is `DONE` only when:

- Acceptance criteria are satisfied.
- Code is merged to the main branch.
- Backend unit, frontend unit/component, and required integration tests pass.
- Contract tests and generated-client drift checks pass when contracts changed.
- Static analysis and security checks pass.
- Database changes use Flyway and have rollback guidance.
- Public APIs, events, web routes, and user-visible states are documented.
- Logs, metrics, and traces cover new behavior.
- Authentication, authorization, ownership, and session-expiry behavior are tested.
- Failure paths are tested.
- Frontend work passes responsive, keyboard, and automated accessibility checks appropriate to its scope.
- `pnpm --dir apps/web build` passes when the web application is affected.
- Documentation is updated.
- Pull request includes exact validation evidence.
- No unresolved P0/P1 defect was introduced.
- Backlog status and dependent items are updated.

---

# 12. Cross-Cutting Acceptance Rules

These rules apply to every relevant ticket.

## API

- Use versioned paths.
- Validate request payloads.
- Return stable error codes.
- Include correlation ID.
- Paginate collections.
- Do not expose stack traces.
- Publish OpenAPI documentation.


## Frontend

- Use the App Router.
- Prefer Server Components and server-side data access.
- Use Client Components only for interaction, browser APIs, or live client state.
- Keep server-only modules out of browser bundles.
- Do not expose access tokens or server secrets to client code.
- Use generated OpenAPI types and clients.
- Represent loading, empty, partial, failure, timeout, and success states.
- Preserve useful state in URLs for filtering and pagination.
- Use semantic HTML, visible focus, keyboard support, and accessible names.
- Avoid hydration-dependent rendering for security decisions.
- Do not duplicate backend state machines or authorization rules.

## Browser-to-Backend Integration

- Browser data calls use the Next.js origin.
- Next.js forwards authenticated requests to the gateway from the server.
- Correlation IDs and idempotency keys are preserved.
- Route Handlers validate shape and session but do not replace backend domain validation.
- Unsafe mutations are never retried automatically without an idempotency guarantee.
- Backend errors retain stable codes and are mapped to safe user messages.

## Database

- Use Flyway.
- Add indexes for documented query patterns.
- Use optimistic locking for mutable aggregates.
- Enforce business uniqueness in the database where possible.
- Use UTC timestamps.
- Avoid cross-service joins.

## Messaging

- Use the standard event envelope.
- Preserve correlation and causation.
- Use stable partition keys.
- Consumers are idempotent.
- Producers use outbox for domain events.
- Define retry and dead-letter behavior.

## Security

- Deny by default.
- Apply least privilege.
- Never log secrets or raw payment data.
- Audit privileged operations.
- Validate token issuer and audience.

## Observability

- Emit structured logs.
- Include trace and correlation identifiers.
- Export technical and business metrics.
- Add actionable errors.
- Link alerts to runbooks.

## Testing

- Cover happy path, validation failure, duplicate delivery, timeout, and concurrency where relevant.
- Prefer Testcontainers for infrastructure integration.
- Keep tests deterministic.
- Do not depend on execution order.
- Preserve test artifacts on CI failure.

---

# 13. Release Quality Gates

The portfolio-ready release cannot ship unless:

- All P0 and P1 tickets are `DONE`.
- Clean checkout builds both Gradle and pnpm workspaces successfully.
- Local environment starts the complete Next.js, gateway, service, identity, messaging, database, and observability stack successfully.
- Kubernetes manifests and Helm charts validate.
- Backend end-to-end and Playwright customer/operations journey suites pass.
- Contract tests pass.
- No unresolved critical vulnerability remains.
- Successful and compensated workflows are demonstrated through the web application.
- Duplicate requests, events, and callbacks are proven safe.
- At least one full distributed trace is captured from browser interaction through Next.js and all participating backend services.
- Dashboards display business and system metrics.
- SQL optimization case study is published.
- Performance results are measured and reproducible.
- Major routes pass automated accessibility checks and documented manual keyboard review.
- Responsive behavior is verified at supported mobile and desktop widths.
- Web performance measurements are published with the test environment.
- Known limitations are documented.
- Demo script and recording are available.

---

# 14. Initial Execution Order

Recommended first sequence:

1. FF-0001 — Initialize monorepo
2. FF-0002 — Common Gradle conventions
3. FF-0004 — Create AGENTS.md
4. FF-2001 — Initialize Next.js application
5. FF-2002 and FF-2004 — Frontend architecture and environment validation
6. FF-0101 — Docker Compose platform
7. FF-0104 — Keycloak realm
8. FF-2101 and FF-2102 — Web authentication and authorization
9. FF-0601 — Event envelope
10. FF-0301 and FF-0401 — Customer and Catalog bootstrap
11. FF-0501 and FF-0502 — Order bootstrap and aggregate
12. FF-0302 through FF-0304 — Customer vertical slice
13. FF-0402 through FF-0404 — Catalog vertical slice
14. FF-0503 and FF-0504 — Synchronous order APIs
15. FF-2103 through FF-2105 — Typed BFF integration
16. FF-2003, FF-2005, and FF-2006 — Design system and application shell
17. FF-2201 through FF-2204 — Dashboard, catalog, checkout, and order timeline
18. FF-0602 through FF-0604 — Kafka foundation
19. FF-0701 and FF-0702 — Outbox and idempotency
20. FF-0801 through FF-0805 — Payment flow
21. FF-0901 through FF-0907 — Provisioning and migration routing
22. FF-1001 through FF-1006 — Saga and recovery
23. FF-2205 and FF-2206 — Cancellation and subscription detail
24. FF-2301 through FF-2306 — Operations console
25. Reliability, observability, security, and full-stack quality epics
26. Kubernetes, CI/CD, performance, migration showcase, and portfolio release

Parallel work is allowed only when dependencies, route ownership, design-system ownership, and contract ownership are clear.

---

# 15. Deferred and Stretch Scope

The following items are intentionally outside the minimum portfolio release unless promoted through a backlog update:

- GraphQL
- Service mesh
- Multi-region deployment
- Event sourcing
- CQRS read-model platform
- Schema Registry infrastructure
- Kubernetes operator
- Chaos engineering platform
- Real payment provider
- Real email delivery
- Native mobile application
- Multi-tenant billing
- Tax calculation
- Usage-based charging
- Machine-learning recommendations

Adding these before the P1 release is complete should be treated as scope risk.

---

# 16. Backlog Maintenance Rules

- Update statuses in the same pull request as implementation handoff.
- Add newly discovered work as a separate backlog item.
- Do not silently expand an existing ticket.
- Split any item estimated above 8 points.
- Link defects to the originating ticket.
- Reassess priorities at milestone boundaries.
- Preserve completed ticket history.
- Record scope removals under `DEFERRED`, not deletion.
- Keep acceptance criteria implementation-neutral unless architecture requires a specific approach.
- Keep the document aligned with ADRs and released behavior.

---

# 17. Portfolio Positioning

The completed project should be described as:

> A full-stack, reliability-focused distributed workflow platform with a Next.js customer portal and operations console. It coordinates customer validation, capacity reservation, payment, and external service provisioning while handling secure web sessions, duplicate delivery, partial failure, compensation, reconciliation, observability, and controlled provider migration.

Avoid positioning it as a simple subscription CRUD application or as a frontend placed over unrelated microservices.

The strongest interview demonstrations are:

- Why Next.js acts as a server-side BFF instead of exposing microservices directly to the browser
- How Keycloak sessions and access tokens are kept out of browser storage
- Why Server Components are the default and where Client Components are justified
- How OpenAPI contract generation keeps the TypeScript client aligned with Spring APIs
- How customer-facing async progress maps to the Saga state machine without duplicating it
- How operations users investigate, retry, replay, and control migration safely
- Why Saga orchestration was selected
- How outbox publishing closes the database-event consistency gap
- How consumers handle at-least-once delivery
- How unsafe retries are prevented across both UI and backend
- How compensation failures remain recoverable and visible
- How provider contracts are isolated from the domain
- How migration traffic is routed and audited
- How the platform recovers after backend or web-runtime restarts
- How traces follow a request from browser interaction through Kafka-backed services
- How accessibility, web performance, and backend performance claims were measured rather than invented
