package io.opengood.autoconfig.openapidocs

import io.opengood.autoconfig.TestApplication
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.*
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Companion.DEFAULT_PATH
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.*
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.Companion.DEFAULT_SECURITY_NAME
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.Oauth2.*
import io.swagger.v3.oas.models.OpenAPI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class OpenApiDocsAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(TestApplication::class.java)
        .withInitializer(ConfigFileApplicationContextInitializer())
        .withConfiguration(AutoConfigurations.of(OpenApiDocsAutoConfiguration::class.java))

    private val openApiDocsProperties = OpenApiDocsProperties(
        enabled = true,
        paths = listOf("/greeting/**"),
        title = "test title",
        description = "test description",
        version = "test version",
        termsOfService = "http://test.tos.url",
        contact = Contact(
            name = "test contact name",
            url = "http://test.contact.url",
            email = "test@domain.com"
        ),
        license = License(
            name = "test license name",
            url = "http://test.lic.url"
        ),
        security = Security(
            enabled = true,
            name = "test security",
            description = "test security description",
            scheme = Scheme.BEARER,
            type = Type.HTTP,
            bearerFormat = BearerFormat.JWT,
            oauth2 = Oauth2(
                grantType = GrantType.CLIENT_CREDENTIALS,
                resource = Resource(
                    authorizationServerUri = "http://localhost/oauth2/authorize"
                ),
                client = Client(
                    scopes = mapOf(
                        Pair("test-1", "test-scope-1"),
                        Pair("test-2", "test-scope-2")
                    )
                ),
                tokenUri = "http://localhost/oauth2/token"
            )
        )
    )

    @Test
    fun `auto configures openapi docs from injected application properties`() {
        val properties = openApiDocsProperties

        val autoConfig = OpenApiDocsAutoConfiguration(properties)

        val openApiConfig = autoConfig.openApi()
        assertThat(openApiConfig?.paths).isEqualTo(getPaths(openApiDocsProperties.paths))
        assertThat(openApiConfig?.info?.title).isEqualTo(openApiDocsProperties.title)
        assertThat(openApiConfig?.info?.description).isEqualTo(openApiDocsProperties.description)
        assertThat(openApiConfig?.info?.version).isEqualTo(openApiDocsProperties.version)
        assertThat(openApiConfig?.info?.termsOfService).isEqualTo(openApiDocsProperties.termsOfService)
        assertThat(openApiConfig?.info?.contact?.name).isEqualTo(openApiDocsProperties.contact.name)
        assertThat(openApiConfig?.info?.contact?.url).isEqualTo(openApiDocsProperties.contact.url)
        assertThat(openApiConfig?.info?.contact?.email).isEqualTo(openApiDocsProperties.contact.email)
        assertThat(openApiConfig?.info?.license?.name).isEqualTo(openApiDocsProperties.license.name)
        assertThat(openApiConfig?.info?.license?.url).isEqualTo(openApiDocsProperties.license.url)

        val securityConfig = openApiConfig?.components?.securitySchemes?.get(openApiDocsProperties.security.name)
        assertThat(securityConfig?.name).isEqualTo(openApiDocsProperties.security.name)
        assertThat(securityConfig?.description).isEqualTo(openApiDocsProperties.security.description)
        assertThat(securityConfig?.scheme).isEqualTo(openApiDocsProperties.security.scheme.toString())
        assertThat(securityConfig?.type).isEqualTo(openApiDocsProperties.security.type.toEnum())
        assertThat(securityConfig?.bearerFormat).isEqualTo(openApiDocsProperties.security.bearerFormat.toString())
    }

    @Test
    fun `auto configures openapi docs from default application properties`() {
        val autoConfig = OpenApiDocsAutoConfiguration(OpenApiDocsProperties())

        val openApiConfig = autoConfig.openApi()
        assertThat(openApiConfig?.paths).isEqualTo(getPaths(listOf(DEFAULT_PATH)))
        assertThat(openApiConfig?.info?.title).isEmpty()
        assertThat(openApiConfig?.info?.description).isEmpty()
        assertThat(openApiConfig?.info?.version).isEmpty()
        assertThat(openApiConfig?.info?.termsOfService).isEmpty()
        assertThat(openApiConfig?.info?.contact?.name).isEmpty()
        assertThat(openApiConfig?.info?.contact?.url).isEmpty()
        assertThat(openApiConfig?.info?.contact?.email).isEmpty()
        assertThat(openApiConfig?.info?.license?.name).isEmpty()
        assertThat(openApiConfig?.info?.license?.url).isEmpty()

        val securityConfig = openApiConfig?.components?.securitySchemes?.get(DEFAULT_SECURITY_NAME)
        assertThat(securityConfig?.name).isEqualTo(DEFAULT_SECURITY_NAME)
        assertThat(securityConfig?.description).isEmpty()
        assertThat(securityConfig?.scheme).isEqualTo(Scheme.BASIC.toString())
        assertThat(securityConfig?.type).isEqualTo(Type.HTTP.toEnum())
        assertThat(securityConfig?.bearerFormat).isEqualTo(BearerFormat.JWT.toString())
    }

    @Test
    fun `auto configures openapi docs from configuration file-based application properties when enabled`() {
        contextRunner
            .withPropertyValues("openapi-docs.enabled=true")
            .run { context: AssertableApplicationContext ->
                assertThat(context).hasSingleBean(OpenAPI::class.java)

                val openApiConfig = context.getBean(OpenAPI::class.java)
                assertThat(openApiConfig.paths).isEqualTo(getPaths(openApiDocsProperties.paths))
                assertThat(openApiConfig.info?.title).isEqualTo(openApiDocsProperties.title)
                assertThat(openApiConfig.info?.description).isEqualTo(openApiDocsProperties.description)
                assertThat(openApiConfig.info?.version).isEqualTo(openApiDocsProperties.version)
                assertThat(openApiConfig.info?.termsOfService).isEqualTo(openApiDocsProperties.termsOfService)
                assertThat(openApiConfig.info?.contact?.name).isEqualTo(openApiDocsProperties.contact.name)
                assertThat(openApiConfig.info?.contact?.url).isEqualTo(openApiDocsProperties.contact.url)
                assertThat(openApiConfig.info?.contact?.email).isEqualTo(openApiDocsProperties.contact.email)
                assertThat(openApiConfig.info?.license?.name).isEqualTo(openApiDocsProperties.license.name)
                assertThat(openApiConfig.info?.license?.url).isEqualTo(openApiDocsProperties.license.url)

                val securityConfig = openApiConfig.components?.securitySchemes?.get(openApiDocsProperties.security.name)
                assertThat(securityConfig?.name).isEqualTo(openApiDocsProperties.security.name)
                assertThat(securityConfig?.description).isEqualTo(openApiDocsProperties.security.description)
                assertThat(securityConfig?.scheme).isEqualTo(openApiDocsProperties.security.scheme.toString())
                assertThat(securityConfig?.type).isEqualTo(openApiDocsProperties.security.type.toEnum())
                assertThat(securityConfig?.bearerFormat).isEqualTo(openApiDocsProperties.security.bearerFormat.toString())
            }
    }

    @Test
    fun `does not auto configure openapi docs from configuration file-based application properties when disabled`() {
        contextRunner
            .withPropertyValues("openapi-docs.enabled=false")
            .run { context: AssertableApplicationContext ->
                assertThat(context).doesNotHaveBean(OpenAPI::class.java)
            }
    }

    @Test
    fun `auto populates openapi docs properties from configuration file-based application properties`() {
        contextRunner
            .withPropertyValues("openapi-docs.enabled=true")
            .run { context: AssertableApplicationContext ->
                assertThat(context).hasSingleBean(OpenApiDocsProperties::class.java)

                val properties = context.getBean(OpenApiDocsProperties::class.java)
                assertThat(properties).isNotNull
                assertThat(properties.enabled).isEqualTo(openApiDocsProperties.enabled)
                assertThat(properties.paths).isEqualTo(openApiDocsProperties.paths)
                assertThat(properties.title).isEqualTo(openApiDocsProperties.title)
                assertThat(properties.description).isEqualTo(openApiDocsProperties.description)
                assertThat(properties.version).isEqualTo(openApiDocsProperties.version)
                assertThat(properties.termsOfService).isEqualTo(openApiDocsProperties.termsOfService)
                assertThat(properties.contact).isNotNull
                assertThat(properties.contact.name).isEqualTo(openApiDocsProperties.contact.name)
                assertThat(properties.contact.url).isEqualTo(openApiDocsProperties.contact.url)
                assertThat(properties.contact.email).isEqualTo(openApiDocsProperties.contact.email)
                assertThat(properties.license).isNotNull
                assertThat(properties.license.name).isEqualTo(openApiDocsProperties.license.name)
                assertThat(properties.license.url).isEqualTo(openApiDocsProperties.license.url)
                assertThat(properties.security).isNotNull
                assertThat(properties.security.enabled).isEqualTo(openApiDocsProperties.security.enabled)
                assertThat(properties.security.name).isEqualTo(openApiDocsProperties.security.name)
                assertThat(properties.security.description).isEqualTo(openApiDocsProperties.security.description)
                assertThat(properties.security.scheme).isEqualTo(openApiDocsProperties.security.scheme)
                assertThat(properties.security.type).isEqualTo(openApiDocsProperties.security.type)
                assertThat(properties.security.bearerFormat).isEqualTo(openApiDocsProperties.security.bearerFormat)
                assertThat(properties.security.oauth2.grantType).isEqualTo(openApiDocsProperties.security.oauth2.grantType)
                assertThat(properties.security.oauth2.resource).isNotNull
                assertThat(properties.security.oauth2.resource.authorizationServerUri).isEqualTo(openApiDocsProperties.security.oauth2.resource.authorizationServerUri)
                assertThat(properties.security.oauth2.client).isNotNull
                assertThat(properties.security.oauth2.client.scopes).isNotEmpty
                assertThat(properties.security.oauth2.client.scopes).isEqualTo(openApiDocsProperties.security.oauth2.client.scopes)
                assertThat(properties.security.oauth2.tokenUri).isEqualTo(openApiDocsProperties.security.oauth2.tokenUri)
            }
    }
}