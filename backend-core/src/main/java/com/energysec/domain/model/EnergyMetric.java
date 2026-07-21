package com.energysec.domain.model;

import java.time.Instant;
import java.util.List;

import com.energysec.domain.exception.InvalidMetricException;

public record EnergyMetric(
        String assetId,
        Instant timestamp,
        List<Double> readings,
        boolean isAnomaly
) {
    public EnergyMetric {
        if (assetId == null || assetId.isBlank()) {
            throw new InvalidMetricException("Asset ID cannot be null or empty");
        }
        if (timestamp == null) {
            throw new InvalidMetricException("Timestamp cannot be null");
        }
        if (readings == null || readings.isEmpty()) {
            throw new InvalidMetricException("Readings cannot be empty");
        }
        // Additional pure domain validations (no Spring/DB dependencies)
    }
    
    // Wither method to return a copy with anomaly flag updated
    public EnergyMetric withAnomaly(boolean anomalyStatus) {
        return new EnergyMetric(assetId, timestamp, readings, anomalyStatus);
    }
}
