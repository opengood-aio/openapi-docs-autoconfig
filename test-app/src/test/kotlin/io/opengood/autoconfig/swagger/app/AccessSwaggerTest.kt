package io.opengood.autoconfig.swagger.app

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [SwaggerTestApplication::class])
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class AccessSwaggerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `swagger UI endpoint is accessible`() {
        mockMvc.perform(get("/swagger-ui.html"))
            .andExpect(status().is2xxSuccessful)
            .andReturn();
    }

    @Test
    fun `swagger API docs endpoint is accessible`() {
        mockMvc.perform(get("/v2/api-docs?group=test-group"))
            .andExpect(status().is2xxSuccessful)
            .andReturn();
    }
}