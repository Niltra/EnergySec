"""Tests de la API del motor ML.

Se configura API_KEY_FILE en un fichero temporal para que la autenticación
se comporte como en producción, sin depender de variables del entorno real.
"""

import pytest
from fastapi.testclient import TestClient

from main import app

API_KEY = "secure-internal-api-key-12345"


@pytest.fixture(autouse=True)
def api_key_file(tmp_path, monkeypatch):
    f = tmp_path / "api_key.txt"
    f.write_text(API_KEY, encoding="utf-8")
    monkeypatch.setenv("API_KEY_FILE", str(f))
    yield


@pytest.fixture
def client():
    # Cada test arranca con los overrides limpios y los deja limpios.
    app.dependency_overrides = {}
    with TestClient(app) as c:
        yield c
    app.dependency_overrides = {}


def test_health_endpoint(client):
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


def test_predict_endpoint_unauthorized(client):
    payload = {"readings": [230.0, 229.5, 230.1]}
    response = client.post("/api/v1/ml/detect", json=payload)
    assert response.status_code == 403


def test_predict_endpoint_wrong_key(client):
    payload = {"readings": [230.0, 229.5, 230.1]}
    headers = {"X-API-Key": "clave-que-no-es"}
    response = client.post("/api/v1/ml/detect", json=payload, headers=headers)
    assert response.status_code == 403


def test_predict_endpoint_authorized_normal(client):
    payload = {"readings": [31000.5, 30500.1, 30800.8, 29900.9, 31200.0]}
    headers = {"X-API-Key": API_KEY}
    response = client.post("/api/v1/ml/detect", json=payload, headers=headers)
    assert response.status_code == 200
    assert response.json() == {"anomaly": False}


def test_predict_endpoint_authorized_anomaly(client):
    payload = {"readings": [30000.0, 29500.5, 12000.2, 11500.5, 13800.0]}
    headers = {"X-API-Key": API_KEY}
    response = client.post("/api/v1/ml/detect", json=payload, headers=headers)
    assert response.status_code == 200
    assert response.json() == {"anomaly": True}
