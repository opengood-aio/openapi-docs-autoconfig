package io.opengood.autoconfig.openapidocs

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class AccessTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `UI endpoint is accessible`() {
        mockMvc.perform(get("/swagger-ui.html"))
            .andExpect(status().is3xxRedirection)
            .andReturn()
    }

    @Test
    fun `API docs endpoint is accessible`() {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().is2xxSuccessful)
            .andReturn()
    }
}