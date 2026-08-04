# FlowForge

FlowForge is a reliability-focused subscription and service-fulfillment platform. A Next.js customer portal and operations console integrate through a server-side BFF with event-driven Spring Boot services.

## Prerequisites

- Java 21
- Node.js 22
- Corepack and pnpm (the pinned version is declared in `package.json`)
- Docker with Docker Compose (required by later platform tickets)

## Build

```bash
./gradlew clean build
corepack enable
pnpm install --frozen-lockfile
pnpm build
```

On Windows, use `gradlew.bat clean build` for the Gradle command.

The authoritative scope, architecture, delivery order, and acceptance criteria are in [`docs/FlowForge_FULLSTACK_BACKLOG.md`](docs/FlowForge_FULLSTACK_BACKLOG.md).

## Repository layout

- `apps/web` — Next.js web application (FF-2001)
- `services` — independently owned Spring Boot service modules
- `libraries` — technical-only shared Java modules
- `provider-simulators` — local external-provider simulators
- `platform` — local and Kubernetes platform assets
- `testing` — cross-service contract, end-to-end, performance, and test data
- `docs` — architecture and delivery documentation
