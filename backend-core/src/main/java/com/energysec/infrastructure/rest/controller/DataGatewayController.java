package com.energysec.infrastructure.rest.controller;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.in.GetSecuredDashboardUseCase;
import com.energysec.domain.port.in.IngestDataUseCase;
import com.energysec.infrastructure.rest.dto.MaskedMetricResponseDTO;
import com.energysec.infrastructure.rest.dto.MetricInputDTO;
import com.energysec.infrastructure.rest.mapper.MetricRestMapper;
import com.energysec.infrastructure.security.masking.ExposeFullData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Tag(name = "Data Gateway", description = "Endpoints para la ingesta y consulta de métricas energéticas")
public class DataGatewayController {

    private final IngestDataUseCase ingestDataUseCase;
    private final GetSecuredDashboardUseCase getSecuredDashboardUseCase;
    private final MetricRestMapper mapper;

    @Operation(summary = "Ingestar nueva métrica energética", description = "Procesa una métrica de forma asíncrona, consultando al motor de IA para detectar anomalías.")
    @ApiResponse(responseCode = "202", description = "Métrica aceptada para procesamiento en background")
    @PostMapping("/ingest")
    public ResponseEntity<Void> ingest(@Valid @RequestBody MetricInputDTO dto) {
        EnergyMetric metric = mapper.toDomain(dto);
        ingestDataUseCase.ingest(metric);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Obtener panel de métricas asegurado", description = "Devuelve el listado de métricas aplicando el AOP de enmascaramiento si el usuario no tiene permisos.")
    @ApiResponse(responseCode = "200", description = "Listado de métricas devuelto exitosamente")
    @GetMapping("/dashboard")
    public ResponseEntity<List<MaskedMetricResponseDTO>> getDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        List<EnergyMetric> metrics = getSecuredDashboardUseCase.getDashboardMetrics(page, size);
        List<MaskedMetricResponseDTO> response = metrics.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener panel en crudo (Solo Admins)", description = "Acceso directo a datos sin ofuscar. Requiere ROLE_ADMIN.")
    @ApiResponse(responseCode = "200", description = "Datos en crudo exportados")
    @GetMapping("/dashboard/raw")
    @ExposeFullData // Only Admin should call this, controlled by Spring Security
    public ResponseEntity<List<MaskedMetricResponseDTO>> getRawDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        List<EnergyMetric> metrics = getSecuredDashboardUseCase.getDashboardMetrics(page, size);
        List<MaskedMetricResponseDTO> response = metrics.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
