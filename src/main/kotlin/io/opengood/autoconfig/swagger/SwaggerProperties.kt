package io.opengood.autoconfig.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "swagger")
data class SwaggerProperties(
    val type: Type = Type.NONE,
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

    enum class Type(val type: String) {
        NONE(""),
        APP("app"),
        SERVICE("service")
    }

    @Configuration
    data class Security(
        val enabled: Boolean = true
    )

    @Configuration
    data class Contact(
        val name: String = "",
        val url: String = "",
        val email: String = ""
    )

    @Configuration
    data class License(
        val type: String = "",
        val url: String = ""
    )

    companion object {
        const val DEFAULT_PATH = ".*"
    }
}
