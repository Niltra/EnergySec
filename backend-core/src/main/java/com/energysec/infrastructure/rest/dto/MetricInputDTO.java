package com.energysec.infrastructure.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class MetricInputDTO {
    @NotNull(message = "El assetId es obligatorio")
    private String assetId;
    private Instant timestamp;
    @NotNull(message = "Las lecturas son obligatorias")
    @Size(min = 1, max = 500, message = "Las lecturas no pueden exceder los 500 data points por chunk")
    private List<Double> readings;
}
