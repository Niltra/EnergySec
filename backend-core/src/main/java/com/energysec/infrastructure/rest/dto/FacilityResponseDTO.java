package com.energysec.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityResponseDTO {
    private String id;
    private String address;
    private String cadastralReference;
    private Double co2Emissions;
    private boolean masked;
}
