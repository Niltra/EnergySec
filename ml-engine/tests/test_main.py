import pytest
from fastapi.testclient import TestClient
from main import app, get_api_key

# Override the dependency to ignore the API_KEY_FILE check during tests
app.dependency_overrides[get_api_key] = lambda: "secure-internal-api-key-12345"

client = TestClient(app)

def test_health_endpoint():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}

def test_predict_endpoint_unauthorized():
    # To test unauthorized, we temporarily remove the override
    app.dependency_overrides = {}
    payload = {"readings": [230.0, 229.5, 230.1]}
    response = client.post("/api/v1/ml/detect", json=payload)
    # Sin X-API-Key, FastAPI security dependency raises 403
    assert response.status_code == 403
    # Restore override for next tests
    app.dependency_overrides[get_api_key] = lambda: "secure-internal-api-key-12345"

def test_predict_endpoint_authorized_normal():
    # ESIOS normal demand is ~30000 MW
    payload = {"readings": [31000.5, 30500.1, 30800.8, 29900.9, 31200.0]}
    headers = {"X-API-Key": "secure-internal-api-key-12345"}
    
    response = client.post("/api/v1/ml/detect", json=payload, headers=headers)
    assert response.status_code == 200
    assert response.json() == {"anomaly": False}

def test_predict_endpoint_authorized_anomaly():
    # ESIOS anomaly demand (e.g. 15000 MW blackout or 48000 MW peak)
    payload = {"readings": [30000.0, 29500.5, 12000.2, 11500.5, 13800.0]}
    headers = {"X-API-Key": "secure-internal-api-key-12345"}
    
    response = client.post("/api/v1/ml/detect", json=payload, headers=headers)
    assert response.status_code == 200
    assert response.json() == {"anomaly": True}
