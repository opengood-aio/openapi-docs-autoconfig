package io.opengood.autoconfig

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greeting")
class GreetingController {

    @Operation(
        summary = "Get greeting for given name",
        parameters = [Parameter(name = "name", description = "The name of entity being greeted")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Request successfully processed",
                content = [Content(mediaType = APPLICATION_JSON_VALUE)]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Request format invalid",
                content = [Content(mediaType = APPLICATION_JSON_VALUE)]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Request failed to process",
                content = [Content(mediaType = APPLICATION_JSON_VALUE)]
            )
        ])
    @Schema(implementation = Greeting::class)
    @GetMapping("/greet/{name}")
    fun greeting(@PathVariable name: String): ResponseEntity<Greeting> {
        return ResponseEntity.ok(Greeting(message = "Hello $name!"))
    }
}
