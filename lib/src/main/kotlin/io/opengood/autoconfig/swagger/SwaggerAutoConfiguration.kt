package io.opengood.autoconfig.swagger

import io.opengood.autoconfig.swagger.SwaggerProperties.Security.Oauth2.Companion.DEFAULT_AUTH_URI
import io.opengood.autoconfig.swagger.SwaggerProperties.Security.Oauth2.Companion.DEFAULT_TOKEN_URI
import io.opengood.autoconfig.swagger.SwaggerProperties.Security.Oauth2.GrantType
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
@ConditionalOnProperty("swagger.enabled", havingValue = "true")
@EnableConfigurationProperties(value = [SwaggerProperties::class])
@EnableSwagger2
class SwaggerAutoConfiguration(val swaggerProperties: SwaggerProperties) {

    val paths = swaggerProperties.paths
        .takeIf { !it.isNullOrEmpty() }
        .let { it?.joinToString(",") } ?: SwaggerProperties.DEFAULT_PATH
    val authUri = swaggerProperties.security.oauth2.resource.authorizationServerUri
        .takeIf { it.isNotBlank() } ?: DEFAULT_AUTH_URI
    val tokenUri = swaggerProperties.security.oauth2.tokenUri
        .takeIf { it.isNotBlank() } ?: DEFAULT_TOKEN_URI

    @Bean
    fun productConfig(): Docket {
        log.info("Setup Swagger product configuration")
        val productConfig = Docket(DocumentationType.SWAGGER_2)
            .groupName(swaggerProperties.groupName)
            .directModelSubstitute(LocalDateTime::class.java, UtilDate::class.java)
            .directModelSubstitute(LocalDate::class.java, SqlDate::class.java)
            .directModelSubstitute(LocalTime::class.java, SqlTime::class.java)
            .apiInfo(apiConfig())
            .select()
            .paths(PathSelectors.regex(paths))
            .build()

        if (swaggerProperties.security.enabled && !authUri.contains("localhost")) {
            productConfig.securitySchemes(listOf(securitySchemes()))
            productConfig.securityContexts(listOf(securityContext()))
        }
        return productConfig
    }

    @Bean
    fun apiConfig(): ApiInfo {
        log.info("Setup Swagger API configuration")
        return ApiInfo(
            swaggerProperties.title,
            swaggerProperties.description,
            swaggerProperties.version,
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
    @ConditionalOnProperty("swagger.security.enabled")
    fun securityConfig(): SecurityConfiguration {
        log.info("Setup Swagger security configuration")
        return if (GrantType.CLIENT_CREDENTIALS == swaggerProperties.security.oauth2.grantType) {
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
        return if (GrantType.CLIENT_CREDENTIALS == swaggerProperties.security.oauth2.grantType) {
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
        return swaggerProperties.security.oauth2.client.scopes
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