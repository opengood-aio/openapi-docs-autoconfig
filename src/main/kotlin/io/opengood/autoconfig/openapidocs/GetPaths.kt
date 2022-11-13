package io.opengood.autoconfig.openapidocs

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths

internal fun getPaths(list: List<String>): Paths {
    val paths = Paths()
    list.forEach { paths.addPathItem(it, PathItem().get(Operation())) }
    return paths
}
