"""In-memory DuckDB over the Molt tables, with the SQL models in sql/."""

from pathlib import Path

import duckdb
import pandas as pd

from .loaders import MoltData

SQL_DIR = Path(__file__).parent / "sql"


def connect(data: MoltData) -> duckdb.DuckDBPyConnection:
    con = duckdb.connect()
    for name, df in data.as_dict().items():
        con.register(name, df)
    return con


def run(con: duckdb.DuckDBPyConnection, model: str) -> pd.DataFrame:
    sql = (SQL_DIR / f"{model}.sql").read_text()
    return con.sql(sql).df()
