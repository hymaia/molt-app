package com.molt.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TalentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listTalentsReturnsOk() throws Exception {
        mockMvc.perform(get("/talents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.page").value(0));
    }

    @Test
    void unknownTalentReturns404() throws Exception {
        mockMvc.perform(get("/talents/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void humanHasNoAgentProfileAndHybridHasOperator() throws Exception {
        mockMvc.perform(get("/talents/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agent").value(nullValue()));
        mockMvc.perform(get("/talents/40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agent.operator.id").value(18));
    }
}
