package com.energysec.infrastructure.rest.controller;

import com.energysec.infrastructure.rest.dto.FinancialResponseDTO;
import com.energysec.infrastructure.security.masking.DataMaskingAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAndValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFailClosedAndMaskDataWhenSecurityContextIsCorrupted() throws Throwable {
        // 1. Simulamos un fallo catastrófico donde el hilo virtual pierde el contexto
        SecurityContextHolder.clearContext();
        
        DataMaskingAspect aspect = new DataMaskingAspect();
        
        // 2. Mockeamos el ProceedingJoinPoint
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        FinancialResponseDTO rawData = new FinancialResponseDTO("1", "B83492019", "Empresa", 150000.0, Instant.now(), false);
        when(joinPoint.proceed()).thenReturn(ResponseEntity.ok(rawData));
        
        // 3. Ejecutamos el aspecto
        ResponseEntity<?> response = (ResponseEntity<?>) aspect.maskDataByDefault(joinPoint);
        FinancialResponseDTO result = (FinancialResponseDTO) response.getBody();
        
        // 4. Verificación Crítica: Ante la duda (null context), SIEMPRE ofuscar.
        assertTrue(result.isMasked());
        assertEquals("B8*******", result.getCif());
        assertEquals(0.0, result.getAmount());
    }

    @Test
    void shouldRejectPayloadWithEmptyOrMassiveReadings() throws Exception {
        String maliciousPayload = "{ \"assetId\": \"SUB-01\", \"readings\": [] }"; // Array vacío (romperá Scikit-Learn)
        
        mockMvc.perform(post("/api/v1/metrics/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(maliciousPayload))
                .andExpect(status().isBadRequest()); // validation fails
    }

    @Test
    void shouldBlockBruteForceAttacksWithHttp429TooManyRequests() throws Exception {
        String ip = "192.168.1.100";
        
        // Consumimos los 100 tokens permitidos en un bucle rápido
        for (int i = 0; i < 100; i++) {
            // Because we don't have authentication mock here for get, it might return 401 or 403, 
            // but the rate limiter filter acts *before* the JWT filter, 
            // so we can still test the rate limit without passing a valid token!
            mockMvc.perform(get("/api/v1/osint/subsidies")
                   .with(request -> { request.setRemoteAddr(ip); return request; }));
        }
        
        // La petición 101 DEBE ser bloqueada por el filtro Bucket4j (status 429)
        mockMvc.perform(get("/api/v1/osint/subsidies")
               .with(request -> { request.setRemoteAddr(ip); return request; }))
               .andExpect(status().isTooManyRequests())
               .andExpect(content().string("Too many requests. Please try again later."));
    }
}
