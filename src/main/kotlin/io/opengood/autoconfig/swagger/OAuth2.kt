package io.opengood.autoconfig.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.HashMap

@Configuration
@ConfigurationProperties(prefix = "swagger.security.oauth2")
data class OAuth2(
    val resource: Resource = Resource(),
    val client: Client = Client(),
    val tokenUri: String = "") {

    @Configuration
    data class Resource(
        val authorizationServerUri: String = ""
    )

    @Configuration
    data class Client(
        val scopes: Map<String, String> = HashMap()
    )

    companion object {
        const val DEFAULT_AUTH_URI = "http://localhost/oauth/authorize"
        const val DEFAULT_TOKEN_URI = "http://localhost/oauth/token"
    }
}