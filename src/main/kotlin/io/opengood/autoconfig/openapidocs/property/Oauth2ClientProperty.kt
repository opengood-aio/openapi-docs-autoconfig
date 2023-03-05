package io.opengood.autoconfig.openapidocs.property

data class Oauth2ClientProperty(
    val scopes: Map<String, String> = HashMap(),
)
