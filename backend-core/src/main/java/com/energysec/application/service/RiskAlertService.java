package com.energysec.application.service;

import com.energysec.domain.model.DgtIncident;
import com.energysec.domain.model.FacilityCertificate;
import com.energysec.domain.model.GeoLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RiskAlertService {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double ALERT_THRESHOLD_KM = 5.0;

    // Simulate our internal database of critical facilities and their coordinates
    private final Map<String, GeoLocation> criticalFacilities = Map.of(
            "SUBSTATION-MAD", new GeoLocation(40.4168, -3.7038), // Madrid Center
            "PLANT-BCN", new GeoLocation(41.3851, 2.1734)        // Barcelona
    );

    /**
     * Procesa un incidente de tráfico de la DGT (o meteo) y evalúa si supone un riesgo.
     */
    public void processTrafficIncident(DgtIncident incident) {
        log.info("Processing new DGT Incident [ID: {} - Type: {}]", incident.incidentId(), incident.type());

        for (Map.Entry<String, GeoLocation> entry : criticalFacilities.entrySet()) {
            double distance = calculateHaversineDistance(entry.getValue(), incident.location());
            
            if (distance <= ALERT_THRESHOLD_KM) {
                log.error("CRITICAL RISK ALERT: Incident '{}' is {:.2f} km from facility '{}'!", 
                        incident.type(), distance, entry.getKey());
                triggerSecurityProtocol(entry.getKey(), incident);
            }
        }
    }

    private void triggerSecurityProtocol(String facilityId, DgtIncident incident) {
        // In a real scenario, this would send an SMS/Email to the SOC, isolate the network, or shutdown non-essential systems.
        log.warn("Security protocol triggered for {}, isolating physical access gates...", facilityId);
    }

    /**
     * Calcula la distancia en Kilómetros entre dos puntos GPS usando la fórmula Haversine.
     */
    public double calculateHaversineDistance(GeoLocation p1, GeoLocation p2) {
        double dLat = Math.toRadians(p2.latitude() - p1.latitude());
        double dLon = Math.toRadians(p2.longitude() - p1.longitude());

        double lat1 = Math.toRadians(p1.latitude());
        double lat2 = Math.toRadians(p2.latitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
