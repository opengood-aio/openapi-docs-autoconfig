package io.opengood.autoconfig.openapidocs

import app.TestApplication
import io.kotest.core.spec.style.WordSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(classes = [TestApplication::class], webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class AccessTest : WordSpec() {

    @Autowired
    lateinit var mockMvc: MockMvc

    override fun extensions() = listOf(SpringExtension)

    init {
        "Service client accessing API endpoint" should {
            "Send valid request and receive success status indicating UI is accessible" {
                mockMvc.get("/swagger-ui.html")
                    .andDo { print() }
                    .andExpect {
                        status { is3xxRedirection() }
                    }
            }

            "Send valid request and receive success status indicating API is accessible" {
                mockMvc.get("/v3/api-docs")
                    .andDo { print() }
                    .andExpect {
                        status { is2xxSuccessful() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                    }
            }
        }
    }
}
