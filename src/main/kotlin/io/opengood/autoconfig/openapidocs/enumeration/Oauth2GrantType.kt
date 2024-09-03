package io.opengood.autoconfig.openapidocs.enumeration

enum class Oauth2GrantType(
    private val value: String,
) {
    AUTHORIZATION_CODE("authorizationCode"),
    CLIENT_CREDENTIALS("clientCredentials"),
    ;

    override fun toString() = value
}
