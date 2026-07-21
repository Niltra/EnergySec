import requests
import time
from datetime import datetime, timezone

# URL del API Gateway (Backend Java)
INGEST_URL = "http://host.docker.internal:8080/api/v1/metrics/ingest"

def send_metric(asset_id, readings, description):
    payload = {
        "assetId": asset_id,
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "readings": readings
    }
    
    print(f"[{description}] Enviando datos de {asset_id}: {readings[:3]}...")
    try:
        response = requests.post(INGEST_URL, json=payload, timeout=5)
        if response.status_code == 202:
            print("  ✅ Ingesta aceptada por el Gateway.")
        else:
            print(f"  ❌ Error: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"  ⚠️ Error de conexión: {e}")
    time.sleep(1)

if __name__ == "__main__":
    print("=== SIMULADOR DE TRÁFICO DE INFRAESTRUCTURA CRÍTICA ===")
    
    # 1. Tráfico Normal (230V)
    send_metric(
        "SUBSTATION-MAD-01", 
        [229.5, 230.1, 230.8, 229.9, 231.0], 
        "TRÁFICO NORMAL"
    )
    
    # 2. Tráfico Normal con ligera fluctuación aceptable
    send_metric(
        "SMART-METER-BCN-88", 
        [225.0, 226.5, 228.1, 222.9, 224.0], 
        "TRÁFICO NORMAL (Fluctuación)"
    )
    
    # 3. ANOMALÍA: Caída extrema de tensión (Brownout / Posible sabotaje)
    send_metric(
        "SUBSTATION-VAL-03", 
        [230.0, 229.5, 140.2, 135.5, 138.0], 
        "⚠️ ANOMALÍA: CAÍDA DE TENSIÓN"
    )
    
    # 4. ANOMALÍA: Pico de tensión destructivo (Sobrecarga inducida)
    send_metric(
        "PLANT-SEV-09", 
        [231.0, 230.5, 310.5, 315.0, 312.2], 
        "⚠️ ANOMALÍA: PICO DE TENSIÓN"
    )
    
    print("\nSimulación finalizada. Revisa el endpoint /dashboard para ver los resultados (recuerda usar el JWT).")
