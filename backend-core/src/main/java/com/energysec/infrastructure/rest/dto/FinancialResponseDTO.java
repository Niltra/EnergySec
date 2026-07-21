package com.energysec.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialResponseDTO {
    private String id;
    private String cif;
    private String companyName;
    private Double amount;
    private Instant timestamp;
    private boolean masked;
}
