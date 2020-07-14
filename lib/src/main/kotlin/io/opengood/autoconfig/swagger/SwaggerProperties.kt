package io.opengood.autoconfig.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "swagger")
@ConstructorBinding
data class SwaggerProperties(
    val enabled: Boolean = true,
    val groupName: String = "",
    val paths: List<String> = listOf(DEFAULT_PATH),
    val title: String = "",
    val description: String = "",
    val version: String = "",
    val termsOfServiceUrl: String = "",
    val security: Security = Security(),
    val contact: Contact = Contact(),
    val license: License = License()
) {
    @ConstructorBinding
    data class Security(
        val enabled: Boolean = false,
        val oauth2: Oauth2 = Oauth2()
    ) {
        @ConstructorBinding
        data class Oauth2(
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
    }

    @ConstructorBinding
    data class Contact(
        val name: String = "",
        val url: String = "",
        val email: String = ""
    )

    @ConstructorBinding
    data class License(
        val type: String = "",
        val url: String = ""
    )

    companion object {
        const val DEFAULT_PATH = ".*"
    }
}
