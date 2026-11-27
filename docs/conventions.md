# Conventions

## General

- `contracts/openapi.yaml` is the source of truth. Change the contract first, then the API, then both clients.
- The web app and the mobile app are iso-functional: a feature ships on both or on neither.
- Money is always stored and transferred in cents (`*Cents`, `int64`). Format it only at the edge.
- A field with a fixed list of values is an `enum` in `openapi.yaml`, mirrored as an enum in every app.

## Git

- Commit messages: `[api]`, `[web]`, `[mobile]`, `[analytics]`, `[contract]` prefix, imperative, under 72 characters.
