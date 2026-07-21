package com.energysec.domain.model;

import java.time.Instant;

public record DgtIncident(
    String incidentId,
    String type,
    GeoLocation location,
    Instant timestamp
) {}
