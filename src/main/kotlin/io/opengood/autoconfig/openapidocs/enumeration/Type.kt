package io.opengood.autoconfig.openapidocs.enumeration

import io.swagger.v3.oas.models.security.SecurityScheme
import java.util.Locale

/**
 * Enumeration of security types for OpenAPI security schemes.
 *
 * This enum defines the supported security types that can be used
 * in OpenAPI security schemes, such as API key and HTTP authentication.
 * It provides conversion to the corresponding SecurityScheme.Type enum.
 */
enum class Type(
    private val value: String,
) {
    APIKEY("apikey"),
    HTTP("http"),
    ;

    /**
     * Returns the string representation of the security type.
     *
     * @return String value of the security type
     */
    override fun toString() = value

    /**
     * Converts this Type enum to the corresponding SecurityScheme.Type enum.
     *
     * This method converts the string value of this enum to uppercase and
     * uses it to find the corresponding value in the SecurityScheme.Type enum.
     *
     * @return The corresponding SecurityScheme.Type enum value
     */
    fun toEnum() = enumValueOf<SecurityScheme.Type>(value.uppercase(Locale.getDefault()))
}
