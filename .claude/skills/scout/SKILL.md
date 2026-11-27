---
name: scout
description: Answer one question across the four Molt apps by sending one app-scout subagent per app, in parallel, then merging their answers into one table. Use when the user runs /scout or asks where something is done across api, web, mobile and analytics.
argument-hint: "<question, e.g. where is the daily rate formatted?>"
disable-model-invocation: true
---

# Scout the four apps

The question is `$ARGUMENTS`. If it is empty, ask for one and stop.

1. In a single message, start **four** `app-scout` subagents in parallel, one per app: `apps/api`, `apps/web`, `apps/mobile`, `apps/analytics`. Give each one the app folder and the question, word for word. Do not search yourself.
2. Wait for the four answers.
3. Merge them into one table: `App | Where | What`, one row per finding, keeping each scout's `path:line`.
4. Under the table, in two or three lines: what the apps do the same way, and what they do differently. Only what the scouts reported.
