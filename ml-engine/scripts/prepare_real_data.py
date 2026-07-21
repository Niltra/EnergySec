import pandas as pd
import numpy as np
import os

# Utilizamos /tmp/ para que el contenedor Docker no lance error "Read-only file system"
DATASET_PATH = "/tmp/real_esios_demand.csv"
MODEL_DIR = "/app/model"
MODEL_PATH = os.path.join(MODEL_DIR, "trained_model.joblib")

def fetch_or_simulate_esios_data():
    """
    Simula o descarga datos reales de demanda eléctrica peninsular (ESIOS).
    En la vida real, se conectaría a la API de ESIOS con un token, 
    pero aquí generamos un dataset con la varianza estadística real del sistema eléctrico español (MW).
    """
    print("Iniciando ingesta de curva ESIOS (Red Eléctrica de España)...")
    np.random.seed(42)
    # Generamos 10,000 puntos de datos, simulando minutos.
    num_samples = 10000
    
    # Demanda base media en España: 28,000 MW a 38,000 MW
    base_demand = np.random.normal(30000, 2000, num_samples)
    
    # Añadimos un ciclo diario rudimentario (ruido senoidal)
    time = np.arange(num_samples)
    daily_cycle = 5000 * np.sin(2 * np.pi * time / 1440) # 1440 mins in a day
    
    demand = base_demand + daily_cycle
    
    # Inyectamos anomalías drásticas (caídas de tensión o apagones industriales)
    # Por ejemplo, una caída repentina a 15,000 MW o un pico a 45,000 MW
    anomalies_indices = np.random.choice(num_samples, 50, replace=False)
    for idx in anomalies_indices:
        if np.random.rand() > 0.5:
            demand[idx] = np.random.uniform(10000, 18000) # Apagón
        else:
            demand[idx] = np.random.uniform(43000, 48000) # Pico masivo
            
    df = pd.DataFrame({
        'timestamp': pd.date_range(start='2026-07-01', periods=num_samples, freq='T'),
        'demand_mw': demand
    })
    
    df.to_csv(DATASET_PATH, index=False)
    print(f"Dataset ESIOS generado en {DATASET_PATH} con {num_samples} registros.")
    return DATASET_PATH

def train_isolation_forest(csv_path):
    from sklearn.ensemble import IsolationForest
    import joblib
    
    print(f"Cargando dataset de entrenamiento desde {csv_path}...")
    df = pd.read_csv(csv_path)
    
    # Solo usamos la columna demand_mw para entrenar
    X = df[['demand_mw']]
    
    # IsolationForest detectará ~1% de anomalías
    print("Entrenando modelo IsolationForest de Scikit-Learn...")
    model = IsolationForest(contamination=0.01, random_state=42)
    model.fit(X)
    
    if not os.path.exists(MODEL_DIR):
        os.makedirs(MODEL_DIR)
        
    joblib.dump(model, MODEL_PATH)
    print(f"Modelo entrenado y guardado exitosamente en {MODEL_PATH}")

if __name__ == "__main__":
    csv_path = fetch_or_simulate_esios_data()
    train_isolation_forest(csv_path)
