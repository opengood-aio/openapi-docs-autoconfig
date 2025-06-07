package io.opengood.autoconfig.openapidocs.property

/**
 * Configuration properties for OAuth2 resource settings in OpenAPI documentation.
 *
 * This class defines the properties for OAuth2 resource settings that will be used
 * in the OpenAPI documentation, including the authorization server URI.
 */
data class Oauth2ResourceProperty(
    val authorizationServerUri: String = DEFAULT_AUTH_URI,
) {
    companion object {
        const val DEFAULT_AUTH_URI = "http://localhost/oauth/authorize"
    }
}
