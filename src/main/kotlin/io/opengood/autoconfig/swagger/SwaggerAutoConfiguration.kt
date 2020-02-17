package io.opengood.autoconfig.swagger

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.AuthorizationCodeGrantBuilder
import springfox.documentation.builders.OAuthBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.sql.Date as SqlDate
import java.sql.Time as SqlTime
import java.util.Date as UtilDate

@Configuration
@ConditionalOnExpression("'\${swagger.type}' == 'app' or '\${swagger.type}' == 'service'")
@EnableSwagger2
class SwaggerAutoConfiguration {
    lateinit var swaggerProperties: SwaggerProperties
    @Autowired(required = false)
    lateinit var swaggerVersion: SwaggerVersion
    @Autowired(required = false)
    lateinit var oAuth2: OAuth2

    lateinit var paths: String
    lateinit var version: String
    lateinit var authUri: String
    lateinit var tokenUri: String

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = getLogger(javaClass.enclosingClass)
    }

    init {
        paths = swaggerProperties.paths.takeIf { it.isNotEmpty() }.let { it?.joinToString(",") }
            ?: SwaggerProperties.DEFAULT_PATH
        version = swaggerVersion.version.takeIf { it.isNotBlank() } ?: swaggerProperties.version
        authUri = oAuth2.resource.authorizationServerUri.takeIf { it.isNotBlank() } ?: OAuth2.DEFAULT_AUTH_URI
        tokenUri = oAuth2.tokenUri.takeIf { it.isNotBlank() } ?: OAuth2.DEFAULT_TOKEN_URI
    }

    @Bean
    fun productApi(): Docket {
        log.info("Setup Swagger product configuration")
        val productApi = Docket(DocumentationType.SWAGGER_2)
            .groupName(swaggerProperties.groupName)
            .directModelSubstitute(LocalDateTime::class.java, UtilDate::class.java)
            .directModelSubstitute(LocalDate::class.java, SqlDate::class.java)
            .directModelSubstitute(LocalTime::class.java, SqlTime::class.java)
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.regex(paths))
            .build()

        if (swaggerProperties.security.enabled && !authUri.contains("localhost")) {
            productApi.securitySchemes(listOf(securitySchemes()))
            productApi.securityContexts(listOf(securityContext()))
        }
        return productApi
    }

    @Bean
    fun apiInfo(): ApiInfo {
        log.info("Setup Swagger API configuration")
        return ApiInfo(
            swaggerProperties.title,
            swaggerProperties.description,
            version,
            swaggerProperties.termsOfServiceUrl,
            Contact(
                swaggerProperties.contact.name,
                swaggerProperties.contact.url,
                swaggerProperties.contact.email),
            swaggerProperties.license.type,
            swaggerProperties.license.url,
            listOf())
    }

    @Bean
    @ConditionalOnExpression("'\${security.oauth2}' != null")
    fun securityInfo(): SecurityConfiguration {
        log.info("Setup Swagger security configuration")
        return if (SwaggerType.SERVICE == swaggerProperties.type) {
            SecurityConfigurationBuilder.builder()
                .clientId(StringUtils.EMPTY)
                .clientSecret(StringUtils.EMPTY)
                .scopeSeparator(" ")
                .build()
        } else {
            SecurityConfigurationBuilder.builder()
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build()
        }
    }

    private fun securitySchemes(): SecurityScheme {
        return if (SwaggerType.SERVICE == swaggerProperties.type) {
            val grantType = ClientCredentialsGrant(authUri)
            OAuthBuilder()
                .name("spring_oauth")
                .grantTypes(listOf(grantType))
                .scopes(scopes())
                .build()
        } else {
            OAuthBuilder()
                .name("spring_oauth")
                .grantTypes(listOf(AuthorizationCodeGrantBuilder()
                    .tokenEndpoint(TokenEndpoint(tokenUri, "oauth_token"))
                    .tokenRequestEndpoint(TokenRequestEndpoint(authUri, "", ""))
                    .build()))
                .scopes(scopes())
                .build()
        }
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(listOf(SecurityReference("spring_oauth", scopes().toTypedArray())))
            .forPaths(PathSelectors.regex(paths))
            .build()
    }

    private fun scopes(): List<AuthorizationScope> {
        return oAuth2.client.scopes
            .takeIf { it.isNotEmpty() }
            .let { it?.values?.map { it -> AuthorizationScope(it, "") } }
            ?: emptyList()
    }
}