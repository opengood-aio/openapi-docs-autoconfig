package io.opengood.autoconfig.openapidocs.property

/**
 * Configuration properties for contact information in OpenAPI documentation.
 *
 * This class defines the properties for contact information that will be displayed
 * in the OpenAPI documentation, including name, URL, and email address.
 */
data class ContactProperty(
    val name: String = "",
    val url: String = "",
    val email: String = "",
)
