import pytest

from molt_analytics.loaders import load_csv


@pytest.fixture(scope="session")
def data():
    return load_csv()
