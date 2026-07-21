package com.energysec.infrastructure.external;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.out.AnomalyDetectionPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class PythonMlEngineAdapter implements AnomalyDetectionPort {

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${app.ml-engine.url}")
    private String mlEngineUrl;
    
    @Value("${app.ml-engine.api-key}")
    private String mlEngineApiKey;

    @Override
    @Async
    @CircuitBreaker(name = "mlEngine", fallbackMethod = "fallbackDetectAnomaly")
    public CompletableFuture<Boolean> detectAnomalyAsync(EnergyMetric metric) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Key", mlEngineApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("readings", metric.readings());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                mlEngineUrl + "/api/v1/ml/detect",
                request,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Boolean isAnomaly = (Boolean) response.getBody().get("anomaly");
            return CompletableFuture.completedFuture(isAnomaly != null ? isAnomaly : false);
        }

        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture<Boolean> fallbackDetectAnomaly(EnergyMetric metric, Throwable t) {
        // Fallback policy: When ML engine is down or circuit is open, we log and return false
        // to avoid Alert Fatigue in the SOC. The metric is marked as normal (false) but could be queued.
        System.err.println("AI Engine Down. Metric " + metric.assetId() + " analysis delayed. Error: " + t.getMessage());
        return CompletableFuture.completedFuture(false);
    }
}
