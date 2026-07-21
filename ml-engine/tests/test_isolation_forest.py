import pytest
import os
import numpy as np
from model.isolation_forest import detect_anomalies

def test_normal_traffic():
    # Simulated normal reading (e.g. European Voltage 30000)
    # The Isolation Forest was trained with data distributed around 30000.
    normal_readings = [29999.5, 30000.1, 30000.8, 29999.9, 30001.0]
    
    # Should not be an anomaly
    assert detect_anomalies(normal_readings) is False

def test_anomalous_traffic_brownout():
    # Simulated brownout / extreme voltage drop
    anomalous_readings = [30000.0, 29999.5, 14000.2, 13500.5, 13800.0]
    
    # Should be flagged as an anomaly
    assert detect_anomalies(anomalous_readings) is True

def test_anomalous_traffic_spike():
    # Simulated destructive voltage spike
    anomalous_readings = [30001.0, 30000.5, 41000.5, 41500.0, 41200.2]
    
    # Should be flagged as an anomaly
    assert detect_anomalies(anomalous_readings) is True

def test_detect_anomalies_nan():
    # Ensure our safety logic catches non-finite values
    with pytest.raises(ValueError, match="Input contains non-finite values"):
        detect_anomalies([30000.0, float('nan'), 30000.0])
