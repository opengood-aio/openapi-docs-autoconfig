package io.opengood.autoconfig.openapidocs

import app.TestApplication
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.spring.SpringListener
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.ApplicationContext

@SpringBootTest(classes = [TestApplication::class], webEnvironment = WebEnvironment.RANDOM_PORT)
class BeanTest : FunSpec() {

    @Autowired
    lateinit var context: ApplicationContext

    override fun listeners() = listOf(SpringListener)

    init {
        test("OpenApiDocsProperties bean is loaded") {
            context.getBean(OpenApiDocsProperties::class.java).shouldNotBeNull()
        }

        test("OpenAPI bean is loaded") {
            context.getBean(OpenAPI::class.java).shouldNotBeNull()
        }
    }
}
