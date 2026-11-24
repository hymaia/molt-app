package com.molt.controller

import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TalentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun listTalentsReturnsOk() {
        mockMvc.perform(get("/talents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.page").value(0))
    }

    @Test
    fun unknownTalentReturns404() {
        mockMvc.perform(get("/talents/999999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("NOT_FOUND"))
    }

    @Test
    fun humanHasNoAgentProfileAndHybridHasOperator() {
        mockMvc.perform(get("/talents/4"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.agent").value(nullValue()))
        mockMvc.perform(get("/talents/40"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.agent.operator.id").value(18))
    }
}
