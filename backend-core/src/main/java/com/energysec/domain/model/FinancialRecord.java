package com.energysec.domain.model;

import java.time.Instant;

public record FinancialRecord(
    String id,
    String cif,
    String companyName,
    Double amount,
    Instant timestamp
) {}
