package io.opengood.autoconfig.openapidocs

import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "openapi-docs")
@ConstructorBinding
data class OpenApiDocsProperties(
    val enabled: Boolean = true,
    val paths: List<String> = listOf(DEFAULT_PATH),
    val title: String = "",
    val description: String = "",
    val version: String = "",
    val termsOfService: String = "",
    val contact: Contact = Contact(),
    val license: License = License(),
    val security: Security = Security()
) {
    data class Contact(
        val name: String = "",
        val url: String = "",
        val email: String = ""
    )

    data class License(
        val name: String = "",
        val url: String = ""
    )

    data class Security(
        val enabled: Boolean = true,
        val name: String = DEFAULT_SECURITY_NAME,
        val description: String = "",
        val scheme: Scheme = Scheme.BASIC,
        val type: Type = Type.HTTP,
        val bearerFormat: BearerFormat = BearerFormat.JWT,
        val oauth2: Oauth2 = Oauth2()
    ) {
        enum class Scheme(private val value: String) {
            BASIC("basic"),
            BEARER("bearer");

            override fun toString() = value
        }

        enum class Type(private val value: String) {
            APIKEY("apikey"),
            HTTP("http");

            override fun toString() = value
            fun toEnum() = enumValueOf<SecurityScheme.Type>(value.toUpperCase())
        }

        enum class BearerFormat(private val value: String) {
            JWT("JWT");

            override fun toString() = value
        }

        data class Oauth2(
            val grantType: GrantType = GrantType.AUTHORIZATION_CODE,
            val resource: Resource = Resource(),
            val client: Client = Client(),
            val tokenUri: String = DEFAULT_TOKEN_URI
        ) {
            enum class GrantType(private val value: String) {
                AUTHORIZATION_CODE("authorizationCode"),
                CLIENT_CREDENTIALS("clientCredentials");

                override fun toString() = value
            }

            data class Resource(
                val authorizationServerUri: String = DEFAULT_AUTH_URI
            )

            data class Client(
                val scopes: Map<String, String> = HashMap()
            )

            companion object {
                const val DEFAULT_AUTH_URI = "http://localhost/oauth/authorize"
                const val DEFAULT_TOKEN_URI = "http://localhost/oauth/token"
            }
        }

        companion object {
            const val DEFAULT_SECURITY_NAME = "default"
        }
    }

    companion object {
        const val DEFAULT_PATH = "/**"
    }
}
