package io.opengood.autoconfig.openapidocs.enumeration

import io.swagger.v3.oas.models.security.SecurityScheme
import java.util.Locale

enum class Type(
    private val value: String,
) {
    APIKEY("apikey"),
    HTTP("http"),
    ;

    override fun toString() = value

    fun toEnum() = enumValueOf<SecurityScheme.Type>(value.uppercase(Locale.getDefault()))
}
