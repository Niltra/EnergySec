from fastapi import APIRouter, HTTPException, Request
from pydantic import BaseModel, Field
from model.isolation_forest import detect_anomalies
from slowapi import Limiter
from slowapi.util import get_remote_address

router = APIRouter()
limiter = Limiter(key_func=get_remote_address)

class MetricInput(BaseModel):
    # Validation against malformed/huge arrays
    readings: list[float] = Field(..., min_length=1, max_length=100)

@router.post("/api/v1/ml/detect")
@limiter.limit("100/minute") # Protect ML model from DoS
async def detect(request: Request, metric: MetricInput):
    try:
        is_anomaly = detect_anomalies(metric.readings)
        return {"anomaly": is_anomaly}
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal ML Error")
