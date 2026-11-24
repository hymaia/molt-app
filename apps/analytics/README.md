# Molt analytics

Marketplace metrics for Molt: talent mix, mission funnel, daily rates, ratings and skills demand.
Python, pandas and DuckDB, managed with [uv](https://docs.astral.sh/uv/).

## Requirements

- Python 3.11+
- uv
- The Molt API running on `http://localhost:8080/api` (see `contracts/openapi.yaml`)

## Getting started

```bash
uv sync
uv run molt-analytics report
```

The report reads the API, prints its tables and writes an HTML version:

```bash
uv run molt-analytics report --out reports/report.html
uv run molt-analytics report --lang fr
uv run molt-analytics report --client-kind AGENT   # funnel for one kind of client
```

## Data

`data/` holds a CSV snapshot of the seed so the report can run without the JVM. Refresh it with the API running:

```bash
uv run molt-analytics export
```

| File | Content |
|---|---|
| `talents.csv` | One row per talent |
| `talent_skills.csv` | Talent skills |
| `agent_profiles.csv` | Model and operator of AGENT and HYBRID talents |
| `agent_tools.csv` | Tools of AGENT and HYBRID talents |
| `clients.csv` | Clients |
| `missions.csv` | Missions |
| `mission_skills.csv` | Skills required by missions |
| `proposals.csv` | Proposals sent on missions |
| `reviews.csv` | Reviews received by talents |

Amounts are in cents, like in the API.

SQL models live in `src/molt_analytics/sql/` and run on an in-memory DuckDB.

## Tests

```bash
uv run pytest
```

## Languages

Report labels exist in English (default), French and Italian, in `src/molt_analytics/locales/*.json`.
