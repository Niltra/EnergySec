package com.energysec.domain.port.out;

import com.energysec.domain.model.EnergyMetric;
import java.util.List;

public interface MetricRepositoryPort {
    void save(EnergyMetric metric);
    List<EnergyMetric> findAll(int page, int size);
}
