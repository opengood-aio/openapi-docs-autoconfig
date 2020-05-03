package io.opengood.autoconfig.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "swagger")
@ConstructorBinding
data class SwaggerProperties(
    val enabled: Boolean = true,
    val groupName: String = "",
    val paths: List<String> = listOf(DEFAULT_PATH),
    val title: String = "",
    val description: String = "",
    val version: String = "",
    val termsOfServiceUrl: String = "",
    val contact: Contact = Contact(),
    val license: License = License()
) {
    @ConstructorBinding
    data class Contact(
        val name: String = "",
        val url: String = "",
        val email: String = ""
    )

    @ConstructorBinding
    data class License(
        val type: String = "",
        val url: String = ""
    )

    companion object {
        const val DEFAULT_PATH = ".*"
    }
}
