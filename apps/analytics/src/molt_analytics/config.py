import os
from pathlib import Path

API_URL = os.environ.get("MOLT_API_URL", "http://localhost:8080/api")

DATA_DIR = Path(__file__).resolve().parents[2] / "data"

# Talents at or above this daily rate are counted as premium.
PREMIUM_DAILY_RATE_EUR = 900

NEW_MISSION_WINDOW_DAYS = 30

DEFAULT_LANG = "en"
