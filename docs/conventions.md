# Conventions

## General

- `contracts/openapi.yaml` is the source of truth. Change the contract first, then the API, then both clients.
- The web app and the mobile app are iso-functional: a feature ships on both or on neither.
- Money is always stored and transferred in cents (`*Cents`, `int64`). Format it only at the edge.

## Git

- Commit messages: `[api]`, `[web]`, `[mobile]`, `[contract]` prefix, imperative, under 72 characters.
