package io.opengood.autoconfig.openapidocs.enumeration

enum class BearerFormat(private val value: String) {
    JWT("JWT"),
    ;

    override fun toString() = value
}
