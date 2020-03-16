package io.opengood.autoconfig.swagger

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [DisableSwaggerController::class])
@ExtendWith(SpringExtension::class)
@ActiveProfiles("disable-swagger")
@AutoConfigureMockMvc
class DisableSwaggerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `swagger UI endpoint is disabled when 'disable-swagger' profile is active`() {
        mockMvc.perform(get("/swagger-ui.html"))
            .andExpect(status().isNotFound)
            .andReturn();
    }

    @Test
    fun `swagger API docs endpoint is disabled when 'disable-swagger' profile is active`() {
        mockMvc.perform(get("/v2/api-docs?group=test-group"))
            .andExpect(status().isNotFound)
            .andReturn();
    }
}