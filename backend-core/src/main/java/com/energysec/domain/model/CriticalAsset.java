package com.energysec.domain.model;

public record CriticalAsset(
        String id,
        String name,
        double latitude,
        double longitude
) {
}
