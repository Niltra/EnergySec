package com.energysec.application.service;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.in.IngestDataUseCase;
import com.energysec.domain.port.out.AnomalyDetectionPort;
import com.energysec.domain.port.out.MetricRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MetricIngestionService implements IngestDataUseCase {

    private final AnomalyDetectionPort anomalyDetectionPort;
    private final MetricRepositoryPort metricRepositoryPort;

    @Override
    @org.springframework.scheduling.annotation.Async
    public void ingest(EnergyMetric metric) {
        // Asynchronous processing: We don't block the ingestion thread
        CompletableFuture<Boolean> anomalyFuture = anomalyDetectionPort.detectAnomalyAsync(metric);
        
        anomalyFuture.thenAccept(isAnomaly -> {
            EnergyMetric processedMetric = metric.withAnomaly(isAnomaly);
            metricRepositoryPort.save(processedMetric);
        }).exceptionally(ex -> {
            // Fallback: If ML Engine fails, save metric but mark for manual review (or as anomaly)
            // Depending on the business rule. Here we default to marking it as true (fail-safe).
            EnergyMetric fallbackMetric = metric.withAnomaly(true);
            metricRepositoryPort.save(fallbackMetric);
            return null;
        });
    }
}
