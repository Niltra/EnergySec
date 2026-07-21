import pandas as pd
import joblib
import os
from sklearn.ensemble import IsolationForest

MODEL_PATH = "model/trained_model.joblib"

def train_model(csv_path: str):
    print(f"Entrenando modelo con datos de: {csv_path}")
    
    # Suponiendo que el CSV tiene una columna 'reading' con valores reales de sensores
    df = pd.read_csv(csv_path)
    if 'reading' not in df.columns:
        raise ValueError("El CSV debe tener una columna 'reading'")
        
    X = df[['reading']].values
    
    # Entrenar Isolation Forest
    clf = IsolationForest(n_estimators=100, contamination=0.01, random_state=42)
    clf.fit(X)
    
    os.makedirs(os.path.dirname(MODEL_PATH), exist_ok=True)
    joblib.dump(clf, MODEL_PATH)
    print(f"Modelo entrenado y guardado en {MODEL_PATH}")

if __name__ == "__main__":
    import argparse
    parser = argparse.ArgumentParser(description='Entrenar modelo ML con datos reales')
    parser.add_argument('csv_path', type=str, help='Ruta al dataset CSV')
    args = parser.parse_args()
    
    train_model(args.csv_path)
