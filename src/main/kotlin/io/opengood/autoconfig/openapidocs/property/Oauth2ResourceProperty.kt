package io.opengood.autoconfig.openapidocs.property

data class Oauth2ResourceProperty(
    val authorizationServerUri: String = DEFAULT_AUTH_URI
) {
    companion object {
        const val DEFAULT_AUTH_URI = "http://localhost/oauth/authorize"
    }
}
