package io.opengood.autoconfig.openapidocs.property

import io.opengood.autoconfig.openapidocs.enumeration.BearerFormat
import io.opengood.autoconfig.openapidocs.enumeration.Scheme
import io.opengood.autoconfig.openapidocs.enumeration.Type

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
