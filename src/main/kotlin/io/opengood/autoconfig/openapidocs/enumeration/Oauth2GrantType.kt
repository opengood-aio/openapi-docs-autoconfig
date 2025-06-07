package io.opengood.autoconfig.openapidocs.enumeration

/**
 * Enumeration of OAuth2 grant types for OpenAPI security schemes.
 *
 * This enum defines the supported OAuth2 grant types that can be used
 * in OpenAPI security schemes, such as authorization code and client credentials.
 */
enum class Oauth2GrantType(
    private val value: String,
) {
    AUTHORIZATION_CODE("authorizationCode"),
    CLIENT_CREDENTIALS("clientCredentials"),
    ;

    /**
     * Returns the string representation of the OAuth2 grant type.
     *
     * @return String value of the OAuth2 grant type
     */
    override fun toString() = value
}
