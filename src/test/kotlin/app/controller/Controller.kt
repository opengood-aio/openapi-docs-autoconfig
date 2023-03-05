package app.controller

import app.model.Greeting
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greeting")
@Tag(
    name = "Greeting Service",
    description = "Provides greeting messages",
)
@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = APPLICATION_JSON_VALUE)],
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = APPLICATION_JSON_VALUE)],
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = APPLICATION_JSON_VALUE)],
        ),
        ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = [Content(mediaType = APPLICATION_JSON_VALUE)],
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = APPLICATION_JSON_VALUE)],
        ),
    ],
)
class Controller {

    @Operation(
        summary = "Get greeting for given name",
        parameters = [Parameter(name = "name", description = "The name of entity being greeted")],
    )
    @Schema(implementation = Greeting::class)
    @GetMapping("/greet/{name}")
    fun greeting(@PathVariable name: String): ResponseEntity<Greeting> {
        return ResponseEntity.ok(Greeting(message = "Hello $name!"))
    }
}
