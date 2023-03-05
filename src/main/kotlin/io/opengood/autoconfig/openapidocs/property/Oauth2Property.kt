package io.opengood.autoconfig.openapidocs.property

import io.opengood.autoconfig.openapidocs.enumeration.Oauth2GrantType

data class Oauth2Property(
    val grantType: Oauth2GrantType = Oauth2GrantType.AUTHORIZATION_CODE,
    val resource: Oauth2ResourceProperty = Oauth2ResourceProperty(),
    val client: Oauth2ClientProperty = Oauth2ClientProperty(),
    val tokenUri: String = DEFAULT_TOKEN_URI,
) {
    companion object {
        const val DEFAULT_TOKEN_URI = "http://localhost/oauth/token"
    }
}
