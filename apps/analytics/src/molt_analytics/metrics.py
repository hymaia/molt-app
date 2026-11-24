"""Marketplace metrics as pure functions over DataFrames.

Every function takes the tables it needs and returns a DataFrame or a scalar.
Money comes in as cents and goes out as euros.
"""

import pandas as pd

KINDS = ["HUMAN", "AGENT", "HYBRID"]
STATUSES = ["OPEN", "CONTRACTED", "DONE"]


def format_eur(cents: float) -> str:
    return f"{cents / 100:,.2f} €".replace(",", " ")


def kind_share(talents: pd.DataFrame) -> pd.DataFrame:
    counts = talents["kind"].value_counts().reindex(KINDS, fill_value=0)
    return pd.DataFrame(
        {
            "kind": counts.index,
            "talents": counts.values,
            "share_pct": (100 * counts / counts.sum()).round(1).values,
        }
    )


def avg_daily_rate_by_kind(talents: pd.DataFrame) -> pd.DataFrame:
    grouped = talents.groupby("kind")["daily_rate_cents"].mean().reindex(KINDS)
    return pd.DataFrame({"kind": grouped.index, "avg_daily_rate_eur": (grouped / 100).round(2).values})


def proposals_per_mission(missions: pd.DataFrame, proposals: pd.DataFrame) -> float:
    counts = proposals.groupby("mission_id").size()
    per_mission = missions["id"].map(counts).fillna(0)
    return round(float(per_mission.mean()), 2)


def mission_funnel(missions: pd.DataFrame) -> pd.DataFrame:
    counts = missions["status"].value_counts().reindex(STATUSES, fill_value=0)
    return pd.DataFrame({"status": counts.index, "missions": counts.values})


def conversion_rate(missions: pd.DataFrame, proposals: pd.DataFrame) -> float:
    """Share of missions that received proposals and then left OPEN."""
    with_proposals = missions[missions["id"].isin(proposals["mission_id"])]
    if with_proposals.empty:
        return 0.0
    converted = with_proposals["status"].isin(["CONTRACTED", "DONE"]).sum()
    return round(100 * converted / len(with_proposals), 1)


def rating_distribution(reviews: pd.DataFrame) -> pd.DataFrame:
    counts = reviews["rating"].value_counts().reindex(range(1, 6), fill_value=0)
    return pd.DataFrame({"stars": counts.index, "reviews": counts.values})
