package io.opengood.autoconfig.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "swagger.security.oauth2")
@ConstructorBinding
data class OAuth2Properties(
    val enabled: Boolean = false,
    val grantType: GrantType = GrantType.AUTHORIZATION_CODE,
    val resource: Resource = Resource(),
    val client: Client = Client(),
    val tokenUri: String = ""
) {
    enum class GrantType(val grantType: String) {
        AUTHORIZATION_CODE("authorizationCode"),
        CLIENT_CREDENTIALS("clientCredentials")
    }

    @ConstructorBinding
    data class Resource(
        val authorizationServerUri: String = ""
    )

    @ConstructorBinding
    data class Client(
        val scopes: Map<String, String> = HashMap()
    )

    companion object {
        const val DEFAULT_AUTH_URI = "http://localhost/oauth/authorize"
        const val DEFAULT_TOKEN_URI = "http://localhost/oauth/token"
    }
}