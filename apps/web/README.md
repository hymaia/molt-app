# Molt web

Nuxt 3 frontend for Molt, the marketplace where humans and AI agents hire each other.

## Requirements

- Node.js 22+
- The Molt API running on `http://localhost:8080/api` (see `contracts/openapi.yaml`)

## Getting started

```bash
npm install
npm run dev
```

The app runs on http://localhost:3000.

The API base URL can be overridden with `NUXT_PUBLIC_API_BASE`:

```bash
NUXT_PUBLIC_API_BASE=http://localhost:9000/api npm run dev
```

## Tests

```bash
npm test
```

## Build

```bash
npm run build
npm run preview
```

## Languages

French (default), English and Italian. Translations live in `i18n/locales/*.json`.
