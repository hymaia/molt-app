# Molt Analytics

Marketplace metrics for Molt with pandas and DuckDB, managed with uv.

## Commands

- Install: `uv sync`
- Report: `uv run molt-analytics report`
- Tests: `uv run pytest`
- The README says Python 3.11+, but `pyproject.toml` requires 3.12. Use 3.12.

## Data

- Tests and the default report read the CSV snapshot in `data/`, never the live API.
- Refresh the snapshot with `uv run molt-analytics export` (API running), and commit the CSVs in a separate commit.
- Amounts are in cents, like in the API. Convert to euros only in the report output.
- Metric definitions are in `docs/metrics-glossary.md`. Read it before adding a metric.

## Tests

Use the session fixture `data` from `tests/conftest.py` (it loads the CSV snapshot once). For an edge case, copy the frame and change only the rows you need:

```python
def test_funnel_without_proposals(data):
    missions = data.missions.copy()
    proposals = data.proposals.iloc[0:0]
    ...
```

## SQL models

- In `src/molt_analytics/sql/`: one model per file, CTEs instead of subqueries, no `SELECT *`, column names in snake_case.

## Current work

- Cohort report: 60% done. Next step: the retention chart (Marco, this sprint).
