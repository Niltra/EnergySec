package com.energysec.domain.port.out;

import com.energysec.domain.model.EnergyMetric;
import java.util.concurrent.CompletableFuture;

public interface AnomalyDetectionPort {
    // Use CompletableFuture to support asynchronous processing
    CompletableFuture<Boolean> detectAnomalyAsync(EnergyMetric metric);
}
