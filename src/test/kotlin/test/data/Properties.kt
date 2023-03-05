package test.data

import io.opengood.autoconfig.openapidocs.enumeration.BearerFormat
import io.opengood.autoconfig.openapidocs.enumeration.Oauth2GrantType
import io.opengood.autoconfig.openapidocs.enumeration.Scheme
import io.opengood.autoconfig.openapidocs.enumeration.Type
import io.opengood.autoconfig.openapidocs.property.ContactProperty
import io.opengood.autoconfig.openapidocs.property.LicenseProperty
import io.opengood.autoconfig.openapidocs.property.Oauth2ClientProperty
import io.opengood.autoconfig.openapidocs.property.Oauth2Property
import io.opengood.autoconfig.openapidocs.property.Oauth2ResourceProperty
import io.opengood.autoconfig.openapidocs.property.OpenApiDocsProperties
import io.opengood.autoconfig.openapidocs.property.SecurityProperty

val openApiDocsProperties = OpenApiDocsProperties(
    enabled = true,
    paths = listOf("/greeting/**"),
    title = "test title",
    description = "test description",
    version = "test version",
    termsOfService = "https://test.tos.url",
    contact = ContactProperty(
        name = "test contact name",
        url = "https://test.contact.url",
        email = "test@domain.com",
    ),
    license = LicenseProperty(
        name = "test license name",
        url = "https://test.lic.url",
    ),
    security = SecurityProperty(
        enabled = true,
        name = "test security",
        description = "test security description",
        scheme = Scheme.BEARER,
        type = Type.HTTP,
        bearerFormat = BearerFormat.JWT,
        oauth2 = Oauth2Property(
            grantType = Oauth2GrantType.CLIENT_CREDENTIALS,
            resource = Oauth2ResourceProperty(
                authorizationServerUri = "http://localhost/oauth2/authorize",
            ),
            client = Oauth2ClientProperty(
                scopes = mapOf(
                    Pair("test-1", "test-scope-1"),
                    Pair("test-2", "test-scope-2"),
                ),
            ),
            tokenUri = "http://localhost/oauth2/token",
        ),
    ),
)
