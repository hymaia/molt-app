"""Load Molt data as pandas DataFrames, from the CSV snapshot or from the API."""

from dataclasses import dataclass, fields
from pathlib import Path

import pandas as pd

from . import config

TABLES = (
    "talents",
    "talent_skills",
    "agent_profiles",
    "agent_tools",
    "clients",
    "missions",
    "mission_skills",
    "proposals",
    "reviews",
)

DATE_COLUMNS = {
    "missions": ["created_at"],
    "proposals": ["created_at"],
    "reviews": ["created_at"],
}


@dataclass(frozen=True)
class MoltData:
    talents: pd.DataFrame
    talent_skills: pd.DataFrame
    agent_profiles: pd.DataFrame
    agent_tools: pd.DataFrame
    clients: pd.DataFrame
    missions: pd.DataFrame
    mission_skills: pd.DataFrame
    proposals: pd.DataFrame
    reviews: pd.DataFrame

    def as_dict(self) -> dict[str, pd.DataFrame]:
        return {f.name: getattr(self, f.name) for f in fields(self)}


def load_csv(data_dir: Path = config.DATA_DIR) -> MoltData:
    frames = {}
    for name in TABLES:
        frames[name] = pd.read_csv(data_dir / f"{name}.csv", parse_dates=DATE_COLUMNS.get(name, False))
    return MoltData(**frames)


def load_api(base_url: str = config.API_URL) -> MoltData:
    from .export import fetch_tables

    frames = fetch_tables(base_url)
    for name, columns in DATE_COLUMNS.items():
        for column in columns:
            frames[name][column] = pd.to_datetime(frames[name][column])
    return MoltData(**frames)
