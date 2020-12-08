package helper

import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.*
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.*
import io.opengood.autoconfig.openapidocs.OpenApiDocsProperties.Security.Oauth2.*

val openApiDocsProperties = OpenApiDocsProperties(
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