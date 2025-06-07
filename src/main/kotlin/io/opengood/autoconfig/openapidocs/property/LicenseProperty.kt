package io.opengood.autoconfig.openapidocs.property

/**
 * Configuration properties for license information in OpenAPI documentation.
 *
 * This class defines the properties for license information that will be displayed
 * in the OpenAPI documentation, including license name and URL.
 */
data class LicenseProperty(
    val name: String = "",
    val url: String = "",
)
