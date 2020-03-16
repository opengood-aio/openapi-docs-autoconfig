package io.opengood.autoconfig.swagger

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class SwaggerAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withInitializer(ConfigFileApplicationContextInitializer())
        .withConfiguration(AutoConfigurations.of(SwaggerAutoConfiguration::class.java))

    @Test
    fun `auto configures swagger from custom application properties and custom version`() {
        val properties = SwaggerProperties(
            groupName = "test group",
            paths = listOf("/test/path"),
            title = "test title",
            description = "test description",
            termsOfServiceUrl = "http://test.tos.url",
            security = SwaggerProperties.Security(
                enabled = true
            ),
            contact = SwaggerProperties.Contact(
                name = "test contact name",
                url = "http://test.contact.url",
                email = "test@domain.com"
            ),
            license = SwaggerProperties.License(
                type = "test license type",
                url = "http://test.lic.url"
            )
        )

        val version = object : SwaggerVersion {
            override val version: String
                get() = "test version"
        }

        val autoConfig = SwaggerAutoConfiguration(properties, version)

        val productApi = autoConfig.productApi()
        assertThat(productApi.groupName).isEqualTo("test group")

        val apiInfo = autoConfig.apiInfo()
        assertThat(apiInfo.title).isEqualTo("test title")
        assertThat(apiInfo.description).isEqualTo("test description")
        assertThat(apiInfo.version).isEqualTo("test version")
        assertThat(apiInfo.termsOfServiceUrl).isEqualTo("http://test.tos.url")
        assertThat(apiInfo.contact.name).isEqualTo("test contact name")
        assertThat(apiInfo.contact.url).isEqualTo("http://test.contact.url")
        assertThat(apiInfo.contact.email).isEqualTo("test@domain.com")
        assertThat(apiInfo.license).isEqualTo("test license type")
        assertThat(apiInfo.licenseUrl).isEqualTo("http://test.lic.url")
    }

    @Test
    fun `auto configures swagger from custom application properties and default version`() {
        val properties = SwaggerProperties(
            groupName = "test group",
            paths = listOf("/test/path"),
            title = "test title",
            description = "test description",
            termsOfServiceUrl = "http://test.tos.url",
            security = SwaggerProperties.Security(
                enabled = true
            ),
            contact = SwaggerProperties.Contact(
                name = "test contact name",
                url = "http://test.contact.url",
                email = "test@domain.com"
            ),
            license = SwaggerProperties.License(
                type = "test license type",
                url = "http://test.lic.url"
            )
        )

        val autoConfig = SwaggerAutoConfiguration(properties)

        val productApi = autoConfig.productApi()
        assertThat(productApi.groupName).isEqualTo("test group")

        val apiInfo = autoConfig.apiInfo()
        assertThat(apiInfo.title).isEqualTo("test title")
        assertThat(apiInfo.description).isEqualTo("test description")
        assertThat(apiInfo.version).isEqualTo("1.0.0")
        assertThat(apiInfo.termsOfServiceUrl).isEqualTo("http://test.tos.url")
        assertThat(apiInfo.contact.name).isEqualTo("test contact name")
        assertThat(apiInfo.contact.url).isEqualTo("http://test.contact.url")
        assertThat(apiInfo.contact.email).isEqualTo("test@domain.com")
        assertThat(apiInfo.license).isEqualTo("test license type")
        assertThat(apiInfo.licenseUrl).isEqualTo("http://test.lic.url")
    }

    @Test
    fun `auto configures swagger from default application properties and default version`() {
        val autoConfig = SwaggerAutoConfiguration()

        val productApi = autoConfig.productApi()
        assertThat(productApi.groupName).isEmpty()

        val apiInfo = autoConfig.apiInfo()
        assertThat(apiInfo.title).isEmpty()
        assertThat(apiInfo.description).isEmpty()
        assertThat(apiInfo.version).isEqualTo("1.0.0")
        assertThat(apiInfo.termsOfServiceUrl).isEmpty()
        assertThat(apiInfo.contact.name).isEmpty()
        assertThat(apiInfo.contact.url).isEmpty()
        assertThat(apiInfo.contact.email).isEmpty()
        assertThat(apiInfo.license).isEmpty()
        assertThat(apiInfo.licenseUrl).isEmpty()
    }

    @Test
    fun `auto configures swagger OAuth2 properties from application properties`() {
        contextRunner
            .withUserConfiguration(SwaggerAutoConfiguration::class.java)
            .run { context: AssertableApplicationContext ->
                assertThat(context).hasSingleBean(OAuth2Properties::class.java)

                val oAuth2Properties = context.getBean(OAuth2Properties::class.java)
                assertThat(oAuth2Properties).isNotNull
                assertThat(oAuth2Properties).isNotNull
                assertThat(oAuth2Properties.grantType).isEqualTo(OAuth2Properties.GrantType.CLIENT_CREDENTIALS)
                assertThat(oAuth2Properties.resource).isNotNull
                assertThat(oAuth2Properties.resource.authorizationServerUri).isEqualTo("http://localhost/oauth2/authorize")
                assertThat(oAuth2Properties.client).isNotNull
                assertThat(oAuth2Properties.client.scopes).isNotEmpty
                assertThat(oAuth2Properties.client.scopes.get("test-1")).isEqualTo("test-scope-1")
                assertThat(oAuth2Properties.client.scopes.get("test-2")).isEqualTo("test-scope-2")
                assertThat(oAuth2Properties.tokenUri).isEqualTo("http://localhost/oauth2/token")
            }
    }
}