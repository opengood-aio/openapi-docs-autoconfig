package io.opengood.autoconfig.openapidocs.enumeration

enum class Scheme(
    private val value: String,
) {
    BASIC("basic"),
    BEARER("bearer"),
    ;

    override fun toString() = value
}
