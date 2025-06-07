package io.opengood.autoconfig.openapidocs.property

import io.opengood.autoconfig.openapidocs.enumeration.BearerFormat
import io.opengood.autoconfig.openapidocs.enumeration.Scheme
import io.opengood.autoconfig.openapidocs.enumeration.Type

/**
 * Configuration properties for security settings in OpenAPI documentation.
 *
 * This class defines the properties for security settings that will be used
 * in the OpenAPI documentation, including whether security is enabled, security name,
 * description, scheme type, bearer format, and OAuth2 configuration.
 */
data class SecurityProperty(
    val enabled: Boolean = true,
    val name: String = DEFAULT_SECURITY_NAME,
    val description: String = "",
    val scheme: Scheme = Scheme.BASIC,
    val type: Type = Type.HTTP,
    val bearerFormat: BearerFormat = BearerFormat.JWT,
    val oauth2: Oauth2Property = Oauth2Property(),
) {
    companion object {
        const val DEFAULT_SECURITY_NAME = "default"
    }
}
