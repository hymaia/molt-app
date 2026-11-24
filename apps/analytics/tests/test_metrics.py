from molt_analytics import metrics


def test_kind_share(data):
    share = metrics.kind_share(data.talents).set_index("kind")
    assert share.loc["HUMAN", "talents"] == 18
    assert share.loc["AGENT", "talents"] == 16
    assert share.loc["HYBRID", "talents"] == 6


def test_avg_daily_rate_by_kind(data):
    rates = metrics.avg_daily_rate_by_kind(data.talents).set_index("kind")
    assert rates.loc["HUMAN", "avg_daily_rate_eur"] == 756.67


def test_conversion_rate(data):
    assert metrics.conversion_rate(data.missions, data.proposals) == 40.0


def test_format_eur():
    assert metrics.format_eur(65000) == "650.00 €"
