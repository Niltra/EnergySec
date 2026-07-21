import numpy as np
import joblib
import os
from sklearn.ensemble import IsolationForest

MODEL_PATH = "model/trained_model.joblib"

# Using joblib instead of pickle for better performance with numpy arrays
# and avoiding arbitrary code execution if the file is replaced by a malicious pickle
# In a real scenario, we should also verify the hash of the model before loading it.

def load_model():
    if not os.path.exists(MODEL_PATH):
        raise FileNotFoundError(f"El modelo no existe en {MODEL_PATH}. Por favor, entrena un modelo con datos reales antes de iniciar el motor ML (ej. usando scripts/train_model.py).")
        
    return joblib.load(MODEL_PATH)

def detect_anomalies(readings: list[float]) -> bool:
    # Validate input for ML safety (No NaNs, Infs)
    arr = np.array(readings, dtype=np.float32)
    if not np.isfinite(arr).all():
        raise ValueError("Input contains non-finite values (NaN, Inf)")
        
    # Reshape for scikit-learn
    X = arr.reshape(-1, 1)
    
    model = load_model()
    # 1 for normal, -1 for anomaly
    predictions = model.predict(X)
    
    # If any reading in the array is an anomaly, flag the whole metric
    return bool(-1 in predictions)
