package io.opengood.autoconfig.openapidocs

import app.TestApplication
import helper.getSecurityScheme
import helper.openApiDocsProperties
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.*
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class OpenApiDocsAutoConfigurationTest : WordSpec({

    "Auto configuration" should {
        val contextRunner = ApplicationContextRunner()
            .withUserConfiguration(TestApplication::class.java)
            .withInitializer(ConfigFileApplicationContextInitializer())
            .withConfiguration(AutoConfigurations.of(OpenApiDocsAutoConfiguration::class.java))

        "Configure OpenAPI docs from injected application properties" {
            val expected = openApiDocsProperties

            val autoConfig = OpenApiDocsAutoConfiguration(expected)

            val openApiConfig = autoConfig.openApi()

            openApiConfig.shouldNotBeNull()
            with(openApiConfig) {
                paths shouldBe getPaths(expected.paths)
                info.shouldNotBeNull()
                with(info) {
                    title shouldBe expected.title
                    description shouldBe expected.description
                    version shouldBe expected.version
                    termsOfService shouldBe expected.termsOfService
                    contact.shouldNotBeNull()
                    with(contact) {
                        name shouldBe expected.contact.name
                        url shouldBe expected.contact.url
                        email shouldBe expected.contact.email
                    }
                    license.shouldNotBeNull()
                    with(license) {
                        name shouldBe expected.license.name
                        url shouldBe expected.license.url
                    }
                }
            }

            val securityConfig = getSecurityScheme(openApiConfig, expected.security.name)

            securityConfig.shouldNotBeNull()
            with(securityConfig) {
                name shouldBe expected.security.name
                description shouldBe expected.security.description
                scheme shouldBe expected.security.scheme.toString()
                type shouldBe expected.security.type.toEnum()
                bearerFormat shouldBe expected.security.bearerFormat.toString()
            }
        }

        "Configure OpenAPI docs from default application properties" {
            val autoConfig = OpenApiDocsAutoConfiguration(OpenApiDocsProperties())

            val openApiConfig = autoConfig.openApi()

            openApiConfig.shouldNotBeNull()
            with(openApiConfig) {
                paths shouldBe getPaths(listOf(OpenApiDocsProperties.DEFAULT_PATH))
                info.shouldNotBeNull()
                with(info) {
                    title.shouldBeEmpty()
                    description.shouldBeEmpty()
                    version.shouldBeEmpty()
                    termsOfService.shouldBeEmpty()
                    contact.shouldNotBeNull()
                    with(contact) {
                        name.shouldBeEmpty()
                        url.shouldBeEmpty()
                        email.shouldBeEmpty()
                    }
                    license.shouldNotBeNull()
                    with(license) {
                        name.shouldBeEmpty()
                        url.shouldBeEmpty()
                    }
                }
            }

            val securityConfig = getSecurityScheme(openApiConfig, OpenApiDocsProperties.Security.DEFAULT_SECURITY_NAME)

            securityConfig.shouldNotBeNull()
            with(securityConfig) {
                name shouldBe OpenApiDocsProperties.Security.DEFAULT_SECURITY_NAME
                description.shouldBeEmpty()
                scheme shouldBe Scheme.BASIC.toString()
                type shouldBe Type.HTTP.toEnum()
                bearerFormat shouldBe BearerFormat.JWT.toString()
            }
        }

        "Configures OpenAPI docs from configuration file-based application properties when enabled" {
            contextRunner
                .withPropertyValues("openapi-docs.enabled=true")
                .run { context ->
                    val expected = openApiDocsProperties

                    val openApiConfig = context.getBean(OpenAPI::class.java)

                    openApiConfig.shouldNotBeNull()
                    with(openApiConfig) {
                        paths shouldBe getPaths(expected.paths)
                        info.shouldNotBeNull()
                        with(info) {
                            title shouldBe expected.title
                            description shouldBe expected.description
                            version shouldBe expected.version
                            termsOfService shouldBe expected.termsOfService
                            contact.shouldNotBeNull()
                            with(contact) {
                                name shouldBe expected.contact.name
                                url shouldBe expected.contact.url
                                email shouldBe expected.contact.email
                            }
                            license.shouldNotBeNull()
                            with(license) {
                                name shouldBe expected.license.name
                                url shouldBe expected.license.url
                            }
                        }
                    }

                    val securityConfig = getSecurityScheme(openApiConfig, expected.security.name)

                    securityConfig.shouldNotBeNull()
                    with(securityConfig) {
                        name shouldBe expected.security.name
                        description shouldBe expected.security.description
                        scheme shouldBe expected.security.scheme.toString()
                        type shouldBe expected.security.type.toEnum()
                        bearerFormat shouldBe expected.security.bearerFormat.toString()
                    }
                }
        }

        "Not configure OpenAPI docs from configuration file-based application properties when disabled" {
            contextRunner
                .withPropertyValues("openapi-docs.enabled=false")
                .run { context ->
                    shouldThrow<NoSuchBeanDefinitionException> { context.getBean(OpenAPI::class.java) }
                }
        }

        "Populate OpenAPI docs properties from configuration file-based application properties" {
            contextRunner
                .withPropertyValues("openapi-docs.enabled=true")
                .run { context ->
                    val expected = openApiDocsProperties

                    val properties = context.getBean(OpenApiDocsProperties::class.java)

                    properties.shouldNotBeNull()
                    with(properties) {
                        enabled shouldBe expected.enabled
                        paths shouldBe expected.paths
                        title shouldBe expected.title
                        description shouldBe expected.description
                        version shouldBe expected.version
                        termsOfService shouldBe expected.termsOfService
                        contact.shouldNotBeNull()
                        with(contact) {
                            name shouldBe expected.contact.name
                            url shouldBe expected.contact.url
                            email shouldBe expected.contact.email
                        }
                        license.shouldNotBeNull()
                        with(license) {
                            name shouldBe expected.license.name
                            url shouldBe expected.license.url
                        }
                        security.shouldNotBeNull()
                        with(security) {
                            enabled shouldBe expected.security.enabled
                            name shouldBe expected.security.name
                            description shouldBe expected.security.description
                            scheme shouldBe expected.security.scheme
                            type shouldBe expected.security.type
                            bearerFormat shouldBe expected.security.bearerFormat
                            oauth2.shouldNotBeNull()
                            with(oauth2) {
                                grantType shouldBe expected.security.oauth2.grantType
                                resource.shouldNotBeNull()
                                with(resource) {
                                    authorizationServerUri shouldBe expected.security.oauth2.resource.authorizationServerUri
                                }
                                client.shouldNotBeNull()
                                with(client) {
                                    scopes.shouldNotBeEmpty()
                                    scopes shouldBe expected.security.oauth2.client.scopes
                                }
                                tokenUri shouldBe expected.security.oauth2.tokenUri
                            }
                        }
                    }
                }
        }
    }
})