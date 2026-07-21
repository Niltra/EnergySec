package com.energysec.domain.port.in;

import com.energysec.domain.model.EnergyMetric;
import java.util.List;

public interface GetSecuredDashboardUseCase {
    // Return a list for dashboard. 
    // Implementing pagination would be better but keeping it simple for the example structure.
    List<EnergyMetric> getDashboardMetrics(int page, int size);
}
