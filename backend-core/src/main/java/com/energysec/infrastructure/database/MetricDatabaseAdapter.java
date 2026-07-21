package com.energysec.infrastructure.database;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.out.MetricRepositoryPort;
import com.energysec.infrastructure.database.entity.EnergyMetricEntity;
import com.energysec.infrastructure.database.repository.SpringDataJpaMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricDatabaseAdapter implements MetricRepositoryPort {

    private final SpringDataJpaMetricRepository repository;

    @Override
    public void save(EnergyMetric metric) {
        EnergyMetricEntity entity = new EnergyMetricEntity();
        entity.setAssetId(metric.assetId());
        entity.setTimestamp(metric.timestamp());
        entity.setReadings(metric.readings());
        entity.setAnomaly(metric.isAnomaly());
        repository.save(entity);
    }

    @Override
    public List<EnergyMetric> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .stream()
                .map(entity -> new EnergyMetric(
                        entity.getAssetId(),
                        entity.getTimestamp(),
                        entity.getReadings(),
                        entity.isAnomaly()
                ))
                .collect(Collectors.toList());
    }
}
