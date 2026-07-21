package com.energysec.application.service;

import com.energysec.domain.model.DgtIncident;
import com.energysec.domain.model.GeoLocation;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiskAlertServiceTest {

    private final RiskAlertService service = new RiskAlertService();

    @Test
    void shouldCalculateCorrectHaversineDistance() {
        GeoLocation madrid = new GeoLocation(40.4168, -3.7038);
        GeoLocation barcelona = new GeoLocation(41.3851, 2.1734);

        double distance = service.calculateHaversineDistance(madrid, barcelona);
        
        // Distance is approx 505 km
        assertTrue(distance > 500 && distance < 510);
    }

    @Test
    void shouldProcessIncidentWithoutErrors() {
        // Incident very close to MADRID substation (40.4168, -3.7038)
        GeoLocation incidentLoc = new GeoLocation(40.4180, -3.7000); // about 0.3 km away
        DgtIncident incident = new DgtIncident("DGT-001", "FIRE", incidentLoc, Instant.now());

        // This should trigger the alert in the logs
        service.processTrafficIncident(incident);
    }
}
