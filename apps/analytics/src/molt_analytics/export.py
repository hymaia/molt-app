"""Snapshot the Molt API into flat CSV files under data/."""

from pathlib import Path

import pandas as pd
import requests

BASE = "http://localhost:8080/api"


def fetch_tables(base_url: str = BASE) -> dict[str, pd.DataFrame]:
    page = requests.get(f"{base_url}/talents", params={"size": 50}, timeout=10).json()

    talents, talent_skills, agent_profiles, agent_tools, reviews = [], [], [], [], []
    for t in page["items"]:
        talents.append(
            {
                "id": t["id"],
                "kind": t["kind"],
                "name": t["name"],
                "title": t["title"],
                "location": t.get("location"),
                "daily_rate_cents": t["dailyRateCents"],
                "rating": t["rating"],
                "mission_count": t["missionCount"],
                "available": t["available"],
            }
        )
        for skill in t["skills"]:
            talent_skills.append({"talent_id": t["id"], "skill": skill})
        if t["agent"]:
            operator = t["agent"].get("operator")
            agent_profiles.append(
                {
                    "talent_id": t["id"],
                    "model": t["agent"]["model"],
                    "operator_id": operator["id"] if operator else None,
                }
            )
            for tool in t["agent"]["tools"]:
                agent_tools.append({"talent_id": t["id"], "tool": tool})
        for r in t.get("reviews", []):
            reviews.append(
                {"talent_id": t["id"], "author": r["author"], "rating": r["rating"], "created_at": r["createdAt"]}
            )

    clients, missions, mission_skills, proposals = {}, [], [], []
    for m in requests.get(f"{base_url}/missions", timeout=10).json():
        detail = requests.get(f"{base_url}/missions/{m['id']}", timeout=10).json()
        clients[m["client"]["id"]] = m["client"]
        missions.append(
            {
                "id": m["id"],
                "title": m["title"],
                "client_id": m["client"]["id"],
                "status": m["status"],
                "duration_days": m["durationDays"],
                "remote": m.get("remote"),
                "created_at": m["createdAt"],
            }
        )
        for skill in m["skills"]:
            mission_skills.append({"mission_id": m["id"], "skill": skill})
        for p in detail["proposals"]:
            proposals.append(
                {
                    "id": p["id"],
                    "mission_id": m["id"],
                    "talent_id": p["talentId"],
                    "daily_rate_eur": p["dailyRateCents"] // 100,
                    "created_at": p["createdAt"],
                }
            )

    return {
        "talents": pd.DataFrame(talents).sort_values("id"),
        "talent_skills": pd.DataFrame(talent_skills),
        "agent_profiles": pd.DataFrame(agent_profiles).astype({"operator_id": "Int64"}),
        "agent_tools": pd.DataFrame(agent_tools),
        "clients": pd.DataFrame(clients.values()).sort_values("id"),
        "missions": pd.DataFrame(missions).sort_values("id"),
        "mission_skills": pd.DataFrame(mission_skills).sort_values(["mission_id", "skill"]),
        "proposals": pd.DataFrame(proposals).sort_values("id"),
        "reviews": pd.DataFrame(reviews),
    }


def export_csv(out_dir: Path, base_url: str = BASE) -> list[Path]:
    out_dir.mkdir(parents=True, exist_ok=True)
    written = []
    for name, df in fetch_tables(base_url).items():
        path = out_dir / f"{name}.csv"
        df.to_csv(path, index=False)
        written.append(path)
    return written
