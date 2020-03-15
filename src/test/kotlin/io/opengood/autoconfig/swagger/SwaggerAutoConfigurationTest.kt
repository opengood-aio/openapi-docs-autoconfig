package io.opengood.autoconfig.swagger

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SwaggerAutoConfigurationTest {

    @Test
    fun `auto configures swagger from custom application properties and custom version`() {
        val properties = SwaggerProperties(
            type = SwaggerProperties.Type.SERVICE,
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

        val product = autoConfig.productApi()
        assertThat(product.groupName).isEqualTo("test group")

        val api = autoConfig.apiInfo()
        assertThat(api.title).isEqualTo("test title")
        assertThat(api.description).isEqualTo("test description")
        assertThat(api.version).isEqualTo("test version")
        assertThat(api.termsOfServiceUrl).isEqualTo("http://test.tos.url")
        assertThat(api.contact.name).isEqualTo("test contact name")
        assertThat(api.contact.url).isEqualTo("http://test.contact.url")
        assertThat(api.contact.email).isEqualTo("test@domain.com")
        assertThat(api.license).isEqualTo("test license type")
        assertThat(api.licenseUrl).isEqualTo("http://test.lic.url")
    }

    @Test
    fun `auto configures swagger from custom application properties and default version`() {
        val properties = SwaggerProperties(
            type = SwaggerProperties.Type.SERVICE,
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

        val product = autoConfig.productApi()
        assertThat(product.groupName).isEqualTo("test group")

        val api = autoConfig.apiInfo()
        assertThat(api.title).isEqualTo("test title")
        assertThat(api.description).isEqualTo("test description")
        assertThat(api.version).isEqualTo("1.0.0")
        assertThat(api.termsOfServiceUrl).isEqualTo("http://test.tos.url")
        assertThat(api.contact.name).isEqualTo("test contact name")
        assertThat(api.contact.url).isEqualTo("http://test.contact.url")
        assertThat(api.contact.email).isEqualTo("test@domain.com")
        assertThat(api.license).isEqualTo("test license type")
        assertThat(api.licenseUrl).isEqualTo("http://test.lic.url")
    }

    @Test
    fun `auto configures swagger from default application properties and default version`() {
        val autoConfig = SwaggerAutoConfiguration()

        val product = autoConfig.productApi()
        assertThat(product.groupName).isEqualTo("")

        val api = autoConfig.apiInfo()
        assertThat(api.title).isEmpty()
        assertThat(api.description).isEmpty()
        assertThat(api.version).isEqualTo("1.0.0")
        assertThat(api.termsOfServiceUrl).isEmpty()
        assertThat(api.contact.name).isEmpty()
        assertThat(api.contact.url).isEmpty()
        assertThat(api.contact.email).isEmpty()
        assertThat(api.license).isEmpty()
        assertThat(api.licenseUrl).isEmpty()
    }

//    val oauth2 = OAuth2(
//        resource = OAuth2.Resource(
//            authorizationServerUri = "http://test.auth-server.uri"
//        ),
//        client = OAuth2.Client(
//            scopes = mapOf(Pair("scope", "test"))
//        ),
//        tokenUri = "http://test.token.uri"
//    )
}