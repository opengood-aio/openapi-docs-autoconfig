package io.opengood.autoconfig.openapidocs

import io.swagger.v3.oas.models.OpenAPI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class BeanLoadTest(val context: ApplicationContext) {

    @Test
    fun `loads openapi docs beans`() {
        assertThat(context.getBean(OpenApiDocsProperties::class.java)).isNotNull
        assertThat(context.getBean(OpenAPI::class.java)).isNotNull
    }
}