package io.opengood.autoconfig.openapidocs.property

/**
 * Configuration properties for OAuth2 client settings in OpenAPI documentation.
 *
 * This class defines the properties for OAuth2 client settings that will be used
 * in the OpenAPI documentation, including the scopes that the client can request.
 */
data class Oauth2ClientProperty(
    val scopes: Map<String, String> = HashMap(),
)
