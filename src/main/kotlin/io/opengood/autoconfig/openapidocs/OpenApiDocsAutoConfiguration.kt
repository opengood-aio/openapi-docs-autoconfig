package io.opengood.autoconfig.openapidocs

import io.opengood.autoconfig.openapidocs.property.OpenApiDocsProperties
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * Spring Boot auto-configuration class for OpenAPI documentation.
 * 
 * This class automatically configures OpenAPI documentation for a Spring Boot application
 * based on the properties defined in OpenApiDocsProperties. It is enabled when the
 * "openapi-docs.enabled" property is set to "true".
 */
@AutoConfiguration
@ConditionalOnProperty("openapi-docs.enabled", havingValue = "true")
@EnableConfigurationProperties(value = [OpenApiDocsProperties::class])
class OpenApiDocsAutoConfiguration(
    private val properties: OpenApiDocsProperties,
) {
    /**
     * Creates and configures an OpenAPI bean based on the provided properties.
     *
     * This method sets up the OpenAPI documentation with information about the API,
     * including title, description, version, contact information, license, and security
     * settings if enabled.
     *
     * @return Configured OpenAPI instance
     */
    @Bean
    fun openApi(): OpenAPI {
        log.info("Setup OpenAPI docs configuration")
        return OpenAPI()
            .paths(getPaths(properties.paths))
            .info(
                Info()
                    .title(properties.title)
                    .description(properties.description)
                    .version(properties.version)
                    .termsOfService(properties.termsOfService)
                    .contact(
                        Contact()
                            .name(properties.contact.name)
                            .url(properties.contact.url)
                            .email(properties.contact.email),
                    ).license(
                        License()
                            .name(properties.license.name)
                            .url(properties.license.url),
                    ),
            ).also {
                if (properties.security.enabled) {
                    it
                        ?.addSecurityItem(
                            SecurityRequirement().addList(properties.security.name),
                        )?.components(
                            Components()
                                .addSecuritySchemes(
                                    properties.security.name,
                                    SecurityScheme()
                                        .name(properties.security.name)
                                        .description(properties.security.description)
                                        .scheme(properties.security.scheme.toString())
                                        .type(properties.security.type.toEnum())
                                        .bearerFormat(properties.security.bearerFormat.toString()),
                                ),
                        )
                }
            }
    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = getLogger(javaClass.enclosingClass)
    }
}
