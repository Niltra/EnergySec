package com.energysec.infrastructure.rest.controller;

import com.energysec.domain.model.FacilityCertificate;
import com.energysec.domain.model.FinancialRecord;
import com.energysec.infrastructure.rest.dto.FacilityResponseDTO;
import com.energysec.infrastructure.rest.dto.FinancialResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.energysec.domain.model.DgtIncident;
import com.energysec.domain.model.GeoLocation;
import com.energysec.application.service.RiskAlertService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/osint")
@RequiredArgsConstructor
public class OsintDataController {

    private final RiskAlertService riskAlertService;

    @GetMapping("/subsidies")
    public ResponseEntity<List<FinancialResponseDTO>> getSubsidies() {
        // Simulating data fetched from BDNS
        FinancialRecord record = new FinancialRecord(
                UUID.randomUUID().toString(),
                "B83492019",
                "Solar Tech Solutions SL",
                150000.0,
                Instant.now()
        );
        
        FinancialResponseDTO dto = new FinancialResponseDTO(
                record.id(),
                record.cif(),
                record.companyName(),
                record.amount(),
                record.timestamp(),
                false
        );
        return ResponseEntity.ok(List.of(dto));
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<FacilityResponseDTO>> getCertificates() {
        // Simulating data fetched from Certificados Energéticos Autonómicos
        FacilityCertificate cert = new FacilityCertificate(
                UUID.randomUUID().toString(),
                "Calle de la Industria 4, Madrid",
                "9872023VK4597S0001WA",
                45.2
        );

        FacilityResponseDTO dto = new FacilityResponseDTO(
                cert.id(),
                cert.address(),
                cert.cadastralReference(),
                cert.co2Emissions(),
                false
        );
        return ResponseEntity.ok(List.of(dto));
    }

    @PostMapping("/dgt/incident")
    public ResponseEntity<Void> receiveDgtIncident(@RequestBody DgtIncident incident) {
        // En la vida real, parsearíamos XML DATEX II. Aquí aceptamos JSON para facilidad de prueba.
        riskAlertService.processTrafficIncident(incident);
        return ResponseEntity.accepted().build();
    }
}
