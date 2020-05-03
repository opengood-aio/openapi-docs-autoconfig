package io.opengood.autoconfig.swagger

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
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
@ConditionalOnProperty("swagger.enabled")
@EnableConfigurationProperties(value = [SwaggerProperties::class, OAuth2Properties::class])
@EnableSwagger2
class SwaggerAutoConfiguration(
    val swaggerProperties: SwaggerProperties = SwaggerProperties(),
    val swaggerVersion: SwaggerVersion = DefaultSwaggerVersion(),
    val oAuth2Properties: OAuth2Properties = OAuth2Properties()
) {
    val paths = swaggerProperties.paths
        .takeIf { !it.isNullOrEmpty() }
        .let { it?.joinToString(",") } ?: SwaggerProperties.DEFAULT_PATH
    val version = swaggerVersion.version
        .takeIf { it.isNotBlank() } ?: swaggerProperties.version
    val authUri = oAuth2Properties.resource.authorizationServerUri
        .takeIf { it.isNotBlank() } ?: OAuth2Properties.DEFAULT_AUTH_URI
    val tokenUri = oAuth2Properties.tokenUri
        .takeIf { it.isNotBlank() } ?: OAuth2Properties.DEFAULT_TOKEN_URI

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

        if (oAuth2Properties.enabled && !authUri.contains("localhost")) {
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
    @ConditionalOnProperty("swagger.security.oauth2.enabled")
    fun securityInfo(): SecurityConfiguration {
        log.info("Setup Swagger security configuration")
        return if (OAuth2Properties.GrantType.CLIENT_CREDENTIALS == oAuth2Properties.grantType) {
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
        return if (OAuth2Properties.GrantType.CLIENT_CREDENTIALS == oAuth2Properties.grantType) {
            OAuthBuilder()
                .name(SECURITY_REFERENCE_NAME)
                .grantTypes(listOf(ClientCredentialsGrant(authUri)))
                .scopes(scopes())
                .build()
        } else {
            OAuthBuilder()
                .name(SECURITY_REFERENCE_NAME)
                .grantTypes(listOf(AuthorizationCodeGrantBuilder()
                    .tokenEndpoint(TokenEndpoint(tokenUri, TOKEN_NAME))
                    .tokenRequestEndpoint(TokenRequestEndpoint(authUri, "", ""))
                    .build()))
                .scopes(scopes())
                .build()
        }
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(listOf(SecurityReference(SECURITY_REFERENCE_NAME, scopes().toTypedArray())))
            .forPaths(PathSelectors.regex(paths))
            .build()
    }

    private fun scopes(): List<AuthorizationScope> {
        return oAuth2Properties.client.scopes
            .takeIf { it.isNotEmpty() }
            .let { it?.values?.map { s -> AuthorizationScope(s, "") } }
            ?: emptyList()
    }

    companion object {
        const val SECURITY_REFERENCE_NAME = "spring_oauth2"
        const val TOKEN_NAME = "oauth2_token"

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = getLogger(javaClass.enclosingClass)
    }
}