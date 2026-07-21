package com.energysec.application.service;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.in.GetSecuredDashboardUseCase;
import com.energysec.domain.port.out.MetricRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardQueryService implements GetSecuredDashboardUseCase {

    private final MetricRepositoryPort metricRepositoryPort;

    @Override
    public List<EnergyMetric> getDashboardMetrics(int page, int size) {
        return metricRepositoryPort.findAll(page, size);
    }
}
