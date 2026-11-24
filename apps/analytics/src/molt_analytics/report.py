"""Marketplace report: KPIs, talent mix, funnel, rates, ratings, skills."""

# %%
import json
from datetime import datetime
from pathlib import Path

import duckdb
import pandas as pd

from . import config, metrics, warehouse
from .loaders import MoltData, load_api

# %%
talents = None
talent_skills = None
agent_profiles = None
agent_tools = None
clients = None
missions = None
mission_skills = None
proposals = None
reviews = None

labels = {}


def load(source="csv"):
    global talents, talent_skills, agent_profiles, agent_tools, clients, missions, mission_skills, proposals, reviews
    if source == "api":
        data = load_api(config.API_URL)
        talents = data.talents
        talent_skills = data.talent_skills
        agent_profiles = data.agent_profiles
        agent_tools = data.agent_tools
        clients = data.clients
        missions = data.missions
        mission_skills = data.mission_skills
        proposals = data.proposals
        reviews = data.reviews
        return

    talents = pd.read_csv("data/talents.csv")
    talent_skills = pd.read_csv("data/talent_skills.csv")
    agent_profiles = pd.read_csv("data/agent_profiles.csv")
    agent_tools = pd.read_csv("data/agent_tools.csv")
    clients = pd.read_csv("data/clients.csv")
    missions = pd.read_csv("data/missions.csv", parse_dates=["created_at"])
    mission_skills = pd.read_csv("data/mission_skills.csv")
    proposals = pd.read_csv("data/proposals.csv")
    reviews = pd.read_csv("data/reviews.csv")


def load_labels(lang):
    global labels
    path = Path(__file__).parent / "locales" / f"{lang}.json"
    raw = json.loads(path.read_text(encoding="utf-8"))
    labels = {}
    for section, entries in raw.items():
        for key, value in entries.items():
            labels[f"{section}.{key}"] = value


def t(key):
    return labels.get(key, key)


# %% headline numbers
def headline():
    ppm = proposals.groupby("mission_id").size().mean()
    premium = (talents["daily_rate_cents"] / 100 >= config.PREMIUM_DAILY_RATE_EUR).sum()
    since = pd.Timestamp.today() - pd.Timedelta(days=config.NEW_MISSION_WINDOW_DAYS)
    new_missions = (missions["created_at"] >= since).sum()
    rows = [
        (t("kpi.talents"), len(talents)),
        (t("kpi.clients"), len(clients)),
        (t("kpi.missions"), len(missions)),
        (t("kpi.proposals"), len(proposals)),
        (t("kpi.proposals_per_mission"), round(ppm, 2)),
        (t("kpi.conversion"), f"{metrics.conversion_rate(missions, proposals)} %"),
        (t("kpi.premium"), int(premium)),
        (t("kpi.new_missions"), int(new_missions)),
    ]
    return pd.DataFrame(rows, columns=["kpi", "value"])


# %% talent mix
def talent_mix(con):
    return warehouse.run(con, "kind_share")


# %% funnel
def funnel(con, client_kind=None):
    if client_kind:
        return con.sql(
            f"""
            select m.status, count(*) as missions
            from missions m
            join clients c on c.id = m.client_id
            where c.kind = '{client_kind}'
            group by m.status
            order by missions desc
            """
        ).df()
    return warehouse.run(con, "funnel")


# %% rates
def rates():
    df = talents.copy()
    df["rate_eur"] = df["daily_rate_cents"] // 100
    listed = df.groupby("kind")["rate_eur"].mean()

    bids = proposals.merge(talents[["id", "kind"]], left_on="talent_id", right_on="id")
    proposed = bids.groupby("kind")["daily_rate_eur"].mean()

    out = pd.DataFrame({"avg_daily_rate": listed, "avg_proposed_rate": proposed}).reset_index()
    out["avg_daily_rate"] = out["avg_daily_rate"].map(lambda v: f"{v:.0f} €")
    out["avg_proposed_rate"] = out["avg_proposed_rate"].map(lambda v: "-" if pd.isna(v) else f"{v:.0f} €")
    return out


# %% ratings
def ratings():
    stars = talents["rating"].round().astype(int)
    dist = stars.value_counts().sort_index()
    return pd.DataFrame({"stars": dist.index, "talents": dist.values})


# %%
def premium_by_kind(con):
    return warehouse.run(con, "premium_by_kind")


def skills_gap(con):
    return warehouse.run(con, "skills_gap")


# %%
def build(source="csv", lang=config.DEFAULT_LANG, output="reports/molt-report.md", client_kind=None):
    load(source)
    load_labels(lang)

    data = MoltData(
        talents=talents,
        talent_skills=talent_skills,
        agent_profiles=agent_profiles,
        agent_tools=agent_tools,
        clients=clients,
        missions=missions,
        mission_skills=mission_skills,
        proposals=proposals,
        reviews=reviews,
    )
    con = warehouse.connect(data)

    sections = [
        (t("section.headline"), headline()),
        (t("section.talent_mix"), talent_mix(con)),
        (t("section.funnel"), funnel(con, client_kind)),
        (t("section.rates"), rates()),
        (t("section.ratings"), ratings()),
        (t("section.premium"), premium_by_kind(con)),
        (t("section.skills_gap"), skills_gap(con)),
    ]

    lines = [f"# {t('report.title')}", "", f"_{t('report.generated')} {datetime.now():%Y-%m-%d %H:%M}_", ""]
    for title, df in sections:
        table = df.to_markdown(index=False)
        print(f"\n== {title} ==")
        print(table)
        lines += [f"## {title}", "", table, ""]

    out = Path(output)
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n".join(lines), encoding="utf-8")
    print(f"\n{t('report.written')} {out}")
    return out


# %%
# con = duckdb.connect()
# duckdb.sql("select kind, avg(daily_rate_cents) from talents group by kind").show()
