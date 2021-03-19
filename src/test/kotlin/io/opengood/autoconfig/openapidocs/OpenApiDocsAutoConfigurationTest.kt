package io.opengood.autoconfig.openapidocs

import app.TestApplication
import data.openApiDocsProperties
import helper.getSecurityScheme
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.BearerFormat
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.Scheme
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.Type
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class OpenApiDocsAutoConfigurationTest : WordSpec({

    "Auto configuration" should {
        val contextRunner = ApplicationContextRunner()
            .withUserConfiguration(TestApplication::class.java)
            .withInitializer(ConfigDataApplicationContextInitializer())
            .withConfiguration(AutoConfigurations.of(OpenApiDocsAutoConfiguration::class.java))

        "Configure OpenAPI docs from injected application properties" {
            val expected = openApiDocsProperties

            val autoConfig = OpenApiDocsAutoConfiguration(expected)

            val openApiConfig = autoConfig.openApi()

            with(openApiConfig) {
                shouldNotBeNull()
                paths shouldBe getPaths(expected.paths)
                with(info) {
                    shouldNotBeNull()
                    title shouldBe expected.title
                    description shouldBe expected.description
                    version shouldBe expected.version
                    termsOfService shouldBe expected.termsOfService
                    with(contact) {
                        shouldNotBeNull()
                        name shouldBe expected.contact.name
                        url shouldBe expected.contact.url
                        email shouldBe expected.contact.email
                    }
                    with(license) {
                        shouldNotBeNull()
                        name shouldBe expected.license.name
                        url shouldBe expected.license.url
                    }
                }
            }

            val securityConfig = getSecurityScheme(openApiConfig, expected.security.name)

            with(securityConfig) {
                shouldNotBeNull()
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

            with(openApiConfig) {
                shouldNotBeNull()
                paths shouldBe getPaths(listOf(OpenApiDocsProperties.DEFAULT_PATH))
                with(info) {
                    shouldNotBeNull()
                    title.shouldBeEmpty()
                    description.shouldBeEmpty()
                    version.shouldBeEmpty()
                    termsOfService.shouldBeEmpty()
                    with(contact) {
                        shouldNotBeNull()
                        name.shouldBeEmpty()
                        url.shouldBeEmpty()
                        email.shouldBeEmpty()
                    }
                    with(license) {
                        shouldNotBeNull()
                        name.shouldBeEmpty()
                        url.shouldBeEmpty()
                    }
                }
            }

            val securityConfig = getSecurityScheme(openApiConfig, OpenApiDocsProperties.Security.DEFAULT_SECURITY_NAME)

            with(securityConfig) {
                shouldNotBeNull()
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

                    with(openApiConfig) {
                        shouldNotBeNull()
                        paths shouldBe getPaths(expected.paths)
                        info.shouldNotBeNull()
                        with(info) {
                            shouldNotBeNull()
                            title shouldBe expected.title
                            description shouldBe expected.description
                            version shouldBe expected.version
                            termsOfService shouldBe expected.termsOfService
                            contact.shouldNotBeNull()
                            with(contact) {
                                shouldNotBeNull()
                                name shouldBe expected.contact.name
                                url shouldBe expected.contact.url
                                email shouldBe expected.contact.email
                            }
                            with(license) {
                                shouldNotBeNull()
                                name shouldBe expected.license.name
                                url shouldBe expected.license.url
                            }
                        }
                    }

                    val securityConfig = getSecurityScheme(openApiConfig, expected.security.name)

                    with(securityConfig) {
                        shouldNotBeNull()
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

                    with(properties) {
                        shouldNotBeNull()
                        enabled shouldBe expected.enabled
                        paths shouldBe expected.paths
                        title shouldBe expected.title
                        description shouldBe expected.description
                        version shouldBe expected.version
                        termsOfService shouldBe expected.termsOfService
                        with(contact) {
                            shouldNotBeNull()
                            name shouldBe expected.contact.name
                            url shouldBe expected.contact.url
                            email shouldBe expected.contact.email
                        }
                        with(license) {
                            shouldNotBeNull()
                            name shouldBe expected.license.name
                            url shouldBe expected.license.url
                        }
                        with(security) {
                            shouldNotBeNull()
                            enabled shouldBe expected.security.enabled
                            name shouldBe expected.security.name
                            description shouldBe expected.security.description
                            scheme shouldBe expected.security.scheme
                            type shouldBe expected.security.type
                            bearerFormat shouldBe expected.security.bearerFormat
                            with(oauth2) {
                                shouldNotBeNull()
                                grantType shouldBe expected.security.oauth2.grantType
                                with(resource) {
                                    shouldNotBeNull()
                                    authorizationServerUri shouldBe expected.security.oauth2.resource.authorizationServerUri
                                }
                                with(client) {
                                    shouldNotBeNull()
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