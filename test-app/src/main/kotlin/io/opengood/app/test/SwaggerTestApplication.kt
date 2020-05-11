package io.opengood.app.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(value = ["io.opengood.autoconfig.swagger", "io.opengood.app.test"])
class SwaggerTestApplication

fun main(args: Array<String>) {
    runApplication<SwaggerTestApplication>(*args)
}
