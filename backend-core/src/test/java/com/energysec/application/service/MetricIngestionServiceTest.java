package com.energysec.application.service;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.out.AnomalyDetectionPort;
import com.energysec.domain.port.out.MetricRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricIngestionServiceTest {

    @Mock
    private MetricRepositoryPort metricRepositoryPort;

    @Mock
    private AnomalyDetectionPort anomalyDetectionPort;

    @InjectMocks
    private MetricIngestionService service;

    @Test
    void shouldIngestNormalMetric() {
        // Arrange
        EnergyMetric metric = new EnergyMetric("SUBSTATION-01", Instant.parse("2026-07-21T12:00:00Z"), List.of(230.1, 230.5), false);

        when(anomalyDetectionPort.detectAnomalyAsync(any(EnergyMetric.class)))
                .thenReturn(CompletableFuture.completedFuture(false));
        doNothing().when(metricRepositoryPort).save(any(EnergyMetric.class));

        // Act
        service.ingest(metric);

        // Assert
        verify(metricRepositoryPort, times(1)).save(any(EnergyMetric.class));
    }

    @Test
    void shouldIngestAnomalousMetricFallback() {
        // Arrange
        EnergyMetric metric = new EnergyMetric("PLANT-09", Instant.parse("2026-07-21T12:05:00Z"), List.of(310.5, 315.0), false);
        
        CompletableFuture<Boolean> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("ML Down"));

        when(anomalyDetectionPort.detectAnomalyAsync(any(EnergyMetric.class))).thenReturn(failedFuture);
        doNothing().when(metricRepositoryPort).save(any(EnergyMetric.class));

        // Act
        service.ingest(metric);

        // Assert
        verify(metricRepositoryPort, times(1)).save(any(EnergyMetric.class));
    }
}
