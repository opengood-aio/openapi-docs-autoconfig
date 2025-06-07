package io.opengood.autoconfig.openapidocs.enumeration

/**
 * Enumeration of security schemes for OpenAPI security definitions.
 *
 * This enum defines the supported security schemes that can be used
 * in OpenAPI security definitions, such as basic and bearer authentication.
 */
enum class Scheme(
    private val value: String,
) {
    BASIC("basic"),
    BEARER("bearer"),
    ;

    /**
     * Returns the string representation of the security scheme.
     *
     * @return String value of the security scheme
     */
    override fun toString() = value
}
