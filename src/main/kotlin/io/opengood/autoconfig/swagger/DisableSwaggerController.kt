package io.opengood.autoconfig.swagger

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@RestController
@Profile("disable-swagger")
class DisableSwaggerController {

    @GetMapping("/swagger-ui.html")
    @Throws(IOException::class)
    fun disableSwaggerUi(response: HttpServletResponse) {
        log.debug("Swagger access disabled for endpoint /swagger-ui.html")
        response.status = HttpStatus.NOT_FOUND.value()
    }

    @GetMapping(value = ["/v2/api-docs"], params = ["group"])
    @Throws(IOException::class)
    fun disableSwaggerApiDocs(response: HttpServletResponse) {
        log.debug("Swagger access disabled for endpoint /v2/api-docs")
        response.status = HttpStatus.NOT_FOUND.value()
    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
    }
}