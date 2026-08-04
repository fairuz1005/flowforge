# FlowForge Web

The Next.js App Router application for the FlowForge customer portal and operations console. Feature routes are intentionally deferred to their backlog tickets.

## Development

From the repository root, run `pnpm dev`. The application is available at `http://localhost:3000`; its unauthenticated health endpoint is `GET /health`.

## Production behavior

- Production browser source maps are disabled so application source is not published by default.
- Next.js build output (`.next`) is ignored and reproduced in CI or deployment builds.
- The health response contains only static service status and exposes no environment values or secrets.
