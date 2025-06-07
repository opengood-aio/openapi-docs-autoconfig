package io.opengood.autoconfig.openapidocs.property

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for OpenAPI documentation.
 *
 * This class defines the configuration properties for OpenAPI documentation with the prefix "openapi-docs".
 * It includes properties for enabling/disabling the documentation, API paths, metadata (title, description, etc.),
 * contact information, license information, and security settings.
 */
@ConfigurationProperties(prefix = "openapi-docs")
data class OpenApiDocsProperties(
    val enabled: Boolean = true,
    val paths: List<String> = listOf(DEFAULT_PATH),
    val title: String = "",
    val description: String = "",
    val version: String = "",
    val termsOfService: String = "",
    val contact: ContactProperty = ContactProperty(),
    val license: LicenseProperty = LicenseProperty(),
    val security: SecurityProperty = SecurityProperty(),
) {
    companion object {
        const val DEFAULT_PATH = "/**"
    }
}
