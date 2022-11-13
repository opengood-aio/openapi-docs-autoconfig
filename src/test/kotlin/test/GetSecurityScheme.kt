package test

import io.swagger.v3.oas.models.OpenAPI

internal fun getSecurityScheme(openApiConfig: OpenAPI, name: String) =
    openApiConfig.components?.securitySchemes?.get(name)
