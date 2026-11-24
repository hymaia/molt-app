from pathlib import Path

from molt_analytics import report


def test_headline(monkeypatch):
    monkeypatch.chdir(Path(__file__).parents[1])
    report.load()
    report.load_labels("en")
    kpis = dict(report.headline().values)
    assert kpis["Talents"] == 40
    assert kpis["Proposals per mission"] == 1.4
