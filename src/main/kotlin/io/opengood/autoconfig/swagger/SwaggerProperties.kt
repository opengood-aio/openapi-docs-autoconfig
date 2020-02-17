package io.opengood.autoconfig.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "swagger")
data class SwaggerProperties(
    val type: SwaggerType = SwaggerType.NONE,
    val groupName: String = "",
    val paths: List<String> = listOf(DEFAULT_PATH),
    val title: String = "",
    val description: String = "",
    val version: String = "",
    val termsOfServiceUrl: String = "",
    val security: SwaggerSecurity = SwaggerSecurity(),
    val contact: SwaggerContact = SwaggerContact(),
    val license: SwaggerLicense = SwaggerLicense()) {

    companion object {
        const val DEFAULT_PATH = ".*"
    }
}

enum class SwaggerType(val type: String) {
    NONE(""),
    APP("app"),
    SERVICE("service")
}

@Configuration
data class SwaggerSecurity(
    val enabled: Boolean = true
)

@Configuration
data class SwaggerContact(
    val name: String = "",
    val url: String = "",
    val email: String = ""
)

@Configuration
data class SwaggerLicense(
    val type: String = "",
    val url: String = ""
)
