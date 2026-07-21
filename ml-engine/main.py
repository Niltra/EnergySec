from fastapi import FastAPI, Depends, HTTPException, Security
from fastapi.security.api_key import APIKeyHeader
from slowapi import Limiter, _rate_limit_exceeded_handler
from slowapi.util import get_remote_address
from slowapi.errors import RateLimitExceeded
from api.routes import router
import os

app = FastAPI(title="Energy ML Engine")

# Rate Limiting
limiter = Limiter(key_func=get_remote_address)
app.state.limiter = limiter
app.add_exception_handler(RateLimitExceeded, _rate_limit_exceeded_handler)

# Authentication
API_KEY_NAME = "X-API-Key"
api_key_header = APIKeyHeader(name=API_KEY_NAME, auto_error=False)

def get_api_key(api_key_header: str = Security(api_key_header)):
    api_key_file = os.getenv("API_KEY_FILE")
    if not api_key_file or not os.path.exists(api_key_file):
        raise HTTPException(status_code=500, detail="API Key not configured")
        
    with open(api_key_file, 'r') as f:
        expected_key = f.read().strip()
        
    if api_key_header == expected_key:
        return api_key_header
    raise HTTPException(status_code=403, detail="Could not validate credentials")

# Include routes
app.include_router(router, dependencies=[Depends(get_api_key)])

@app.get("/health")
def health_check():
    return {"status": "ok"}
