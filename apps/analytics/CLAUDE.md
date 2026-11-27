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
- Categorical columns (`kind`, `status`) stay as labels: group and filter on the label.
- Metric definitions are in `docs/metrics-glossary.md`. Read it before adding a metric.

## Engineering Guidelines

Molt Analytics turns the raw data of the marketplace into metrics the team can act on. Every
number in the report ends up in a product discussion, so correctness and clarity matter more
than speed of delivery. The guidelines below describe how we expect code to be written in
this project. Please read them before making a change and keep them in mind while you work.

The project is written in Python, chosen for its readability and its mature ecosystem for
data analysis. Write idiomatic, modern Python: follow PEP 8, use four spaces for
indentation, keep functions short and focused, and prefer explicit code over clever code.
Group imports with the standard library first, then third-party packages, then local
modules, and avoid wildcard imports. Use f-strings for formatting and pathlib for file
paths. Remove unused imports, dead code and debugging prints before you commit.

We follow the usual design principles. Each module and each function should have a single
responsibility. Avoid duplication, but do not introduce an abstraction until the same logic
appears at least twice. Keep things simple, and do not build features nobody asked for.
Prefer composition over inheritance and pure functions over functions with side effects:
a function that takes DataFrames and returns a DataFrame is easier to test and to reuse
than one that reads files or calls the network.

Naming is part of the design. Use snake_case for variables and functions, PascalCase for
classes and UPPER_SNAKE_CASE for constants. Choose names that describe what a value is, not
how it is computed. Function names should start with a verb, boolean names should read as
a question, and abbreviations should be avoided unless they are standard in the domain.

Use type hints on every public function, with the modern syntax (`list[str]`, `X | None`),
and annotate DataFrames as `pd.DataFrame`. Every public module, class and function should
have a docstring that starts with a one-line summary and describes the parameters, the
return value and the exceptions raised. Comments should explain why the code does
something, not what it does.

pandas is our main tool for data manipulation. Prefer vectorized operations to loops, and
avoid `iterrows()` and `apply()` with Python functions on large frames. Use `.loc` for
label-based selection and avoid chained assignment, which can silently modify a copy
instead of the original frame. Call `.copy()` when you need an independent frame. Handle
missing values explicitly with `isna()`, `fillna()` or `dropna()` rather than letting them
propagate. Choose appropriate dtypes, and convert dates with `pd.to_datetime` as early as
possible. Method chaining often makes a transformation easier to read.

DuckDB handles the heavier aggregations. It is an in-process analytical database: it needs
no server, it is columnar, and it can query pandas DataFrames directly. Use it when a
transformation is clearer in SQL than in pandas, typically for multi-step aggregations and
window functions. Always pass values as query parameters rather than formatting them into
SQL strings, and close connections when you are done with them. Write SQL that is easy to
read: one clause per line, meaningful aliases and consistent casing.

Errors should be handled deliberately. Never use a bare `except:`, catch the most specific
exception you can, and never swallow an exception without logging it. When you re-raise,
use `raise ... from ...` so the original traceback is kept. Validate inputs at the edges of
the system: data coming from the API or from files can be incomplete or malformed. Use the
`logging` module rather than `print` for diagnostics, choose the right level, and never log
personal data.

Tests are part of the definition of done. Every change should come with tests, and every
bug fix with a test that fails without the fix. Tests should be fast, isolated and
deterministic. Follow the Arrange-Act-Assert structure, give each test a name that
describes the behavior it checks, and cover edge cases such as empty frames, missing values
and boundary values. Test behavior, not implementation details. Aim for high coverage on
the metrics, but do not write tests only to raise a number. Run the full suite before you
push.

Performance matters, but measure before you optimize. Load only the columns you need,
filter as early as possible, push heavy aggregations to DuckDB, and avoid materializing
large intermediate frames. Cache expensive computations only when profiling shows they are
a bottleneck.

Security is everyone's job. Never commit secrets, tokens or credentials; read configuration
from environment variables. Keep dependencies up to date and treat every external input as
untrusted. Manage dependencies with uv, keep their number small, prefer well-maintained
libraries, and never edit the lock file by hand.

Work in small, reviewable increments. Create a branch for each change, write commit
messages in the imperative mood that explain the intent of the change, rebase on the main
branch before opening a merge request, and never force-push a shared branch. Before asking
for a review, check that the code runs, the tests pass, the edge cases are handled and the
documentation is up to date. Keep the README and this file current when conventions change.

When you are unsure about a requirement, ask rather than guess. Explain your reasoning when
you propose a change, keep the scope of each task focused, and summarize what you changed
at the end so that the rest of the team can follow your work.

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
