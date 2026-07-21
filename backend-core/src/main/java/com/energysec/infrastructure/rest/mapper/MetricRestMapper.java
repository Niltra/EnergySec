package com.energysec.infrastructure.rest.mapper;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.infrastructure.rest.dto.MaskedMetricResponseDTO;
import com.energysec.infrastructure.rest.dto.MetricInputDTO;
import org.springframework.stereotype.Component;

@Component
public class MetricRestMapper {

    public EnergyMetric toDomain(MetricInputDTO dto) {
        return new EnergyMetric(
                dto.getAssetId(),
                dto.getTimestamp(),
                dto.getReadings(),
                false // default on ingestion
        );
    }

    public MaskedMetricResponseDTO toDto(EnergyMetric domain) {
        return new MaskedMetricResponseDTO(
                domain.assetId(),
                domain.timestamp(),
                domain.readings(),
                domain.isAnomaly(),
                false
        );
    }
}
