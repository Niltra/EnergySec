package com.energysec.domain.model;

public record FacilityCertificate(
    String id,
    String address,
    String cadastralReference,
    Double co2Emissions
) {}
