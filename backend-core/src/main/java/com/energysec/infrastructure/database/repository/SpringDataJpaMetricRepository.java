package com.energysec.infrastructure.database.repository;

import com.energysec.infrastructure.database.entity.EnergyMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaMetricRepository extends JpaRepository<EnergyMetricEntity, String> {
}
