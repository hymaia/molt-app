# Molt

Marketplace where humans and AI agents post missions, send proposals, and hire each other.

## Project

- `apps/api`: Kotlin, Java 17, Spring Boot, Maven, JPA, H2.
- `apps/web`: Nuxt 3, Vue 3, Pinia.
- `apps/mobile`: Flutter.
- `apps/analytics`: Python, uv, pandas, DuckDB.
- `contracts/openapi.yaml`: API contract.
- `docs/conventions.md`: shared conventions.

## Commands

Run from the repository root. Install the relevant app dependencies first.

```bash
npm install                         # Root tooling, including contract tests
npm --prefix apps/web install       # Web dependencies
(cd apps/mobile && flutter pub get)
(cd apps/analytics && uv sync)

npm run dev:api                     # API on port 8080
npm run dev:web                     # Web on port 3000
npm run dev:mobile                  # Flutter in Chrome
npm run dev:analytics               # Analytics report

npm run test:api
npm run test:web
npm run test:mobile
npm run test:analytics
npm run test:contract               # Requires the API running on port 8080
npm --prefix apps/web run build
```

## Code increments

- Ask which layer to change when the scope is unclear (API, web, mobile, analytics, or OpenAPI contract). Do not assume the web app is the target because there is a package.json at the root.

## Shared rules

- Follow `contracts/openapi.yaml`. Read the affected endpoint before coding.
- When API behavior changes, update the contract first, then the API and both clients.
- Check whether API data changes also affect analytics.
- Keep field names, enums, null values, dates, and errors consistent across apps.
- Web and mobile must offer the same features. If asked to change only one, flag the impact on the other.
- Ask before making breaking API changes, destructive database changes, or large architecture changes.
- Store and send money in integer cents (`*Cents`). Format it for display without dropping cents.
- Do not rely on demo data order or generated IDs.

## Workflow

1. Read the contract, app instructions, and relevant code and tests.
2. Check which apps need changes.
3. Make a small, focused change. Reuse existing rules and mappings.
4. Add tests for new behavior and bug fixes, including errors and edge cases.
5. Run tests and build checks for affected apps. Run contract tests for API changes.
6. Update any affected documentation.
7. Report what changed, what passed, and what was not checked.

Use test fixtures, not demo seed IDs. Test concurrent writes when needed.
Do not weaken tests to make them pass or call unfinished work complete.

## Scope and safety

- Preserve unrelated user changes.
- Ask before deleting data or running destructive migrations.
- Commit messages: `[api]`, `[web]`, `[mobile]`, `[analytics]`, or `[contract]`; imperative wording; under 72 characters.
