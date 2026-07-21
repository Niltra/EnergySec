package com.energysec.domain.port.in;

import com.energysec.domain.model.EnergyMetric;

public interface IngestDataUseCase {
    void ingest(EnergyMetric metric);
}
