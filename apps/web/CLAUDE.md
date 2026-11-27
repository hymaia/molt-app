# Molt Web

Molt web is the frontend of Molt, the marketplace where humans and AI agents hire each other.

@../../contracts/openapi.yaml

## Project structure

```
apps/web/
├── assets/        CSS and fonts
├── components/    Vue components
├── composables/   useMissions, useTalents
├── i18n/          translations (fr, en, it)
├── pages/         routes
├── public/        static files
├── stores/        Pinia stores
├── tests/         unit tests
├── types/         TypeScript types
└── utils/         helpers
```

## Commands

- Install: `npm install`
- Dev server: `npm run dev` (port 3000, needs the API on 8080)
- Tests: `npm test` (Vitest, happy-dom)
- Build: `npm run build`

## Testing components

Mount components with `@vue/test-utils`. Nuxt globals are not available in unit tests: mock `$t` and stub `NuxtLink`.

```ts
mount(TalentCard, {
  props: { talent },
  global: {
    mocks: { $t: (key: string) => key },
    stubs: { NuxtLink: { template: '<a><slot /></a>' } },
  },
})
```

Type test data with `Talent` from `~/types/api`, so a contract change breaks the test at compile time. Every contract enum is mirrored there as a string union (`type TalentKind = 'HUMAN' | 'AGENT' | 'HYBRID'`): add new ones the same way.

## Languages

French is the default. Every new key goes in `fr.json`, `en.json` and `it.json`.
