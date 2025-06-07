package io.opengood.autoconfig.openapidocs.enumeration

/**
 * Enumeration of bearer token formats for OpenAPI security schemes.
 *
 * This enum defines the supported bearer token formats that can be used
 * in OpenAPI security schemes, such as JWT.
 */
enum class BearerFormat(
    private val value: String,
) {
    JWT("JWT"),
    ;

    /**
     * Returns the string representation of the bearer format.
     *
     * @return String value of the bearer format
     */
    override fun toString() = value
}
