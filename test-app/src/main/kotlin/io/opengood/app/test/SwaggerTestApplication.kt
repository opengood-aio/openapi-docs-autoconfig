package io.opengood.app.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SwaggerTestApplication

fun main(args: Array<String>) {
    runApplication<SwaggerTestApplication>(*args)
}
