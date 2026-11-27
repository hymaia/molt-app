---
name: translation-checker
description: Verify that every English UI string has French and Italian translations in Molt's web JSON locales (apps/web/i18n/locales) and mobile Flutter ARB locales (apps/mobile/lib/l10n). Use whenever adding, changing, reviewing, or releasing user-facing text, translations, i18n, l10n, locale files, or language support in this repository. Runs a deterministic checker, then proposes French and Italian text for each missing or broken translation.
compatibility: Requires Node.js 18 or later. No dependencies.
---

# Translation Checker

Run this whenever English copy or locale files change, and before release.
Web and mobile must both cover every English message (see root `CLAUDE.md`:
web and mobile offer the same features).

## 1. Run the deterministic check

From the repository root:

```bash
node .claude/skills/translation-checker/scripts/check-translations.mjs
```

Options: `--json` for machine-readable output, `--root <dir>` to check another
checkout (e.g. a `git archive HEAD` export to check committed state only).

It compares `en` against `fr` and `it`:

- web: `apps/web/i18n/locales/{en,fr,it}.json` (nested keys, flattened as `a.b.c`)
- mobile: `apps/mobile/lib/l10n/app_{en,fr,it}.arb` (keys starting with `@` are metadata, ignored)

| Level | Status | Meaning |
|-------|--------|---------|
| ERROR | `file-error` | File absent or invalid JSON |
| ERROR | `missing` | Key in English, absent in target |
| ERROR | `empty` | Target value blank |
| ERROR | `type-mismatch` / `invalid-source-type` | Value is not a string |
| ERROR | `placeholder-mismatch` | Variables differ (`{count}` renamed or dropped) |
| WARN  | `same-as-english` | Target identical to English: copied, or a legit cognate |
| WARN  | `extra-key` | Key in target, absent in English (stale) |

Exit code `1` on any ERROR, `0` otherwise. Warnings never fail the check.

Why `missing` matters on mobile: `flutter gen-l10n` silently falls back to
English for keys absent from `app_it.arb`, so the app builds and ships English
text. Check the `.arb` files, not the generated `app_localizations_*.dart`.

## 2. Propose translations

For each ERROR, and each `same-as-english` WARN that is not a real cognate
or product name (e.g. `Molt`, `Missions` in French, `Agent` are fine):

1. Read the English source and nearby keys in the target file for tone and
   terminology (reuse existing translations of the same words).
2. Propose French and/or Italian text that keeps:
   - every placeholder name unchanged (`{count}`, `{name}`)
   - ICU plural/select structure in ARB (`{count, plural, =0{…} other{…}}`)
   - vue-i18n pipe plurals in web JSON (`1 giorno | {count} giorni`)
   - product names, Markdown/HTML tokens, and UI intent (button vs sentence)
3. Use the same wording for the same key across web and mobile.

## 3. Apply or report

- **Review-only task**: do not edit files. Report each finding:

  ```
  - [web / it] `missions.remote`  (apps/web/i18n/locales/it.json)
    English: "Remote"
    Suggested: "Da remoto"
  ```

- **Edit authorized**: add the value at the same key/position as in `en`.
  In ARB, keep `@@locale` and existing metadata. Then:
  1. Re-run the checker until it exits `0`.
  2. Mobile changed: `cd apps/mobile && flutter gen-l10n`, confirm the
     generated `app_localizations_<locale>.dart` now has the translation.
  3. Commit per client: `[web] …`, `[mobile] …`.

## Report format

State clean or not. If not, group findings by client then locale, with the
proposed text. List warnings you judged acceptable (cognates) separately.
The checker verifies presence, non-empty strings and placeholder names; it
does not judge translation quality. Never claim a pass when a file could not
be read.

## Self-test

```bash
node --test .claude/skills/translation-checker/scripts/check-translations.test.mjs
```
