package com.energysec.infrastructure.rest.controller;

import com.energysec.domain.model.EnergyMetric;
import com.energysec.domain.port.in.GetSecuredDashboardUseCase;
import com.energysec.domain.port.in.IngestDataUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MetricControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngestDataUseCase ingestUseCase;

    @MockBean
    private GetSecuredDashboardUseCase dashboardUseCase;

    @Test
    void shouldAcceptIngestWithoutAuth() throws Exception {
        String payload = """
                {
                  "assetId": "SUBSTATION-MAD",
                  "timestamp": "2026-07-21T12:00:00Z",
                  "readings": [230.1, 230.2]
                }
                """;

        Mockito.doNothing().when(ingestUseCase).ingest(any(EnergyMetric.class));

        mockMvc.perform(post("/api/v1/metrics/ingest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldDenyDashboardWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/metrics/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowDashboardWithAdminAuth() throws Exception {
        Mockito.when(dashboardUseCase.getDashboardMetrics(0, 50))
                .thenReturn(List.of(new EnergyMetric("SUBS****", Instant.parse("2026-07-21T12:00:00Z"), List.of(230.1), false)));

        mockMvc.perform(get("/api/v1/metrics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assetId").value("SUBS****"));
    }
}
