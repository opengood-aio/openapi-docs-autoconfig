package io.opengood.autoconfig.openapidocs.property

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
    val contact: ContactProperty = ContactProperty(),
    val license: LicenseProperty = LicenseProperty(),
    val security: SecurityProperty = SecurityProperty()
) {
    companion object {
        const val DEFAULT_PATH = "/**"
    }
}
