package io.opengood.autoconfig.swagger

import io.opengood.autoconfig.swagger.SwaggerProperties.Security.Oauth2.GrantType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import springfox.documentation.service.ApiInfo
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration

class SwaggerAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withInitializer(ConfigFileApplicationContextInitializer())
        .withConfiguration(AutoConfigurations.of(SwaggerAutoConfiguration::class.java))

    @Test
    fun `auto configures swagger from injected application properties`() {
        val properties = SwaggerProperties(
            enabled = true,
            groupName = "test group",
            paths = listOf("/test/path"),
            title = "test title",
            description = "test description",
            version = "test version",
            termsOfServiceUrl = "http://test.tos.url",
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

        val productApi = autoConfig.productConfig()
        assertThat(productApi.groupName).isEqualTo("test group")

        val apiInfo = autoConfig.apiConfig()
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
    fun `auto configures swagger from default application properties`() {
        val autoConfig = SwaggerAutoConfiguration(SwaggerProperties())

        val productApi = autoConfig.productConfig()
        assertThat(productApi.groupName).isEmpty()

        val apiInfo = autoConfig.apiConfig()
        assertThat(apiInfo.title).isEmpty()
        assertThat(apiInfo.description).isEmpty()
        assertThat(apiInfo.version).isEmpty()
        assertThat(apiInfo.termsOfServiceUrl).isEmpty()
        assertThat(apiInfo.contact.name).isEmpty()
        assertThat(apiInfo.contact.url).isEmpty()
        assertThat(apiInfo.contact.email).isEmpty()
        assertThat(apiInfo.license).isEmpty()
        assertThat(apiInfo.licenseUrl).isEmpty()
    }

    @Test
    fun `auto configures swagger from configuration file-based application properties when swagger enabled with security OAuth2 client credentials grant type`() {
        contextRunner
            .withPropertyValues("swagger.enabled=true")
            .run { context: AssertableApplicationContext ->
                assertThat(context).hasSingleBean(Docket::class.java)

                val productConfig = context.getBean(Docket::class.java)
                assertThat(productConfig.groupName).isEqualTo("test group")

                assertThat(context).hasSingleBean(ApiInfo::class.java)

                val apiConfig = context.getBean(ApiInfo::class.java)
                assertThat(apiConfig.title).isEqualTo("test title")
                assertThat(apiConfig.description).isEqualTo("test description")
                assertThat(apiConfig.version).isEqualTo("test version")
                assertThat(apiConfig.termsOfServiceUrl).isEqualTo("http://test.tos.url")
                assertThat(apiConfig.contact.name).isEqualTo("test contact name")
                assertThat(apiConfig.contact.url).isEqualTo("http://test.contact.url")
                assertThat(apiConfig.contact.email).isEqualTo("test@domain.com")
                assertThat(apiConfig.license).isEqualTo("test license type")
                assertThat(apiConfig.licenseUrl).isEqualTo("http://test.lic.url")

                assertThat(context).hasSingleBean(SecurityConfiguration::class.java)

                val securityConfig = context.getBean(SecurityConfiguration::class.java)
                assertThat(securityConfig.clientId).isEmpty()
                assertThat(securityConfig.clientSecret).isEmpty()
                assertThat(securityConfig.scopeSeparator()).isEqualTo(" ")
            }
    }

    @Test
    fun `auto configures swagger from configuration file-based application properties when swagger enabled with security OAuth2 authorization code grant type`() {
        contextRunner
            .withPropertyValues(
                "swagger.enabled=true",
                "swagger.security.oauth2.grant-type=authorizationCode"
            )
            .run { context: AssertableApplicationContext ->
                assertThat(context).hasSingleBean(Docket::class.java)

                val productConfig = context.getBean(Docket::class.java)
                assertThat(productConfig.groupName).isEqualTo("test group")

                assertThat(context).hasSingleBean(ApiInfo::class.java)

                val apiConfig = context.getBean(ApiInfo::class.java)
                assertThat(apiConfig.title).isEqualTo("test title")
                assertThat(apiConfig.description).isEqualTo("test description")
                assertThat(apiConfig.version).isEqualTo("test version")
                assertThat(apiConfig.termsOfServiceUrl).isEqualTo("http://test.tos.url")
                assertThat(apiConfig.contact.name).isEqualTo("test contact name")
                assertThat(apiConfig.contact.url).isEqualTo("http://test.contact.url")
                assertThat(apiConfig.contact.email).isEqualTo("test@domain.com")
                assertThat(apiConfig.license).isEqualTo("test license type")
                assertThat(apiConfig.licenseUrl).isEqualTo("http://test.lic.url")

                assertThat(context).hasSingleBean(SecurityConfiguration::class.java)

                val securityConfig = context.getBean(SecurityConfiguration::class.java)
                assertThat(securityConfig.useBasicAuthenticationWithAccessCodeGrant).isTrue()
            }
    }

    @Test
    fun `does not auto configure swagger from configuration file-based application properties when swagger disabled`() {
        contextRunner
            .withPropertyValues("swagger.enabled=false")
            .run { context: AssertableApplicationContext ->
                assertThat(context).doesNotHaveBean(Docket::class.java)
                assertThat(context).doesNotHaveBean(ApiInfo::class.java)
                assertThat(context).doesNotHaveBean(SecurityConfiguration::class.java)
            }
    }

    @Test
    fun `auto populates swagger properties from configuration file-based application properties`() {
        contextRunner
            .withPropertyValues("swagger.enabled=true")
            .run { context: AssertableApplicationContext ->
                assertThat(context).hasSingleBean(SwaggerProperties::class.java)

                val swaggerProperties = context.getBean(SwaggerProperties::class.java)
                assertThat(swaggerProperties).isNotNull
                assertThat(swaggerProperties.enabled).isTrue()
                assertThat(swaggerProperties.groupName).isEqualTo("test group")
                assertThat(swaggerProperties.paths).isEqualTo(listOf("/test/path1", "/test/path2"))
                assertThat(swaggerProperties.title).isEqualTo("test title")
                assertThat(swaggerProperties.description).isEqualTo("test description")
                assertThat(swaggerProperties.version).isEqualTo("test version")
                assertThat(swaggerProperties.termsOfServiceUrl).isEqualTo("http://test.tos.url")
                assertThat(swaggerProperties.security).isNotNull
                assertThat(swaggerProperties.security.enabled).isTrue()
                assertThat(swaggerProperties.security.oauth2.grantType).isEqualTo(GrantType.CLIENT_CREDENTIALS)
                assertThat(swaggerProperties.security.oauth2.resource).isNotNull
                assertThat(swaggerProperties.security.oauth2.resource.authorizationServerUri).isEqualTo("http://localhost/oauth2/authorize")
                assertThat(swaggerProperties.security.oauth2.client).isNotNull
                assertThat(swaggerProperties.security.oauth2.client.scopes).isNotEmpty
                assertThat(swaggerProperties.security.oauth2.client.scopes["test-1"]).isEqualTo("test-scope-1")
                assertThat(swaggerProperties.security.oauth2.client.scopes["test-2"]).isEqualTo("test-scope-2")
                assertThat(swaggerProperties.security.oauth2.tokenUri).isEqualTo("http://localhost/oauth2/token")
                assertThat(swaggerProperties.contact).isNotNull
                assertThat(swaggerProperties.contact.name).isEqualTo("test contact name")
                assertThat(swaggerProperties.contact.url).isEqualTo("http://test.contact.url")
                assertThat(swaggerProperties.contact.email).isEqualTo("test@domain.com")
                assertThat(swaggerProperties.license).isNotNull
                assertThat(swaggerProperties.license.type).isEqualTo("test license type")
                assertThat(swaggerProperties.license.url).isEqualTo("http://test.lic.url")
            }
    }
}