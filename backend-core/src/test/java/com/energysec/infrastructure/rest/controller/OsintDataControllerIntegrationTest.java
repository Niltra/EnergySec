package com.energysec.infrastructure.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disabling security filters to test just the Aspect masking
@ActiveProfiles("test")
class OsintDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldMaskSubsidiesForAnonymousUsers() throws Exception {
        mockMvc.perform(get("/api/v1/osint/subsidies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].masked").value(true))
                .andExpect(jsonPath("$[0].cif").value("B8*******"))
                .andExpect(jsonPath("$[0].amount").value(0.0));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldNotMaskSubsidiesForAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/osint/subsidies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].masked").value(false))
                .andExpect(jsonPath("$[0].cif").value("B83492019"))
                .andExpect(jsonPath("$[0].amount").value(150000.0));
    }

    @Test
    void shouldMaskCertificatesForAnonymousUsers() throws Exception {
        mockMvc.perform(get("/api/v1/osint/certificates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].masked").value(true))
                .andExpect(jsonPath("$[0].cadastralReference").value("98720***********"));
    }
}
