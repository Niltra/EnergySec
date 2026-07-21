package com.energysec.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaskedMetricResponseDTO {
    private String assetId;
    private Instant timestamp;
    private List<Double> readings;
    private boolean isAnomaly;
    
    // Additional field to indicate if data was masked
    private boolean masked;
}
