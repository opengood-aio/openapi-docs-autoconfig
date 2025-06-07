package io.opengood.autoconfig.openapidocs

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths

/**
 * Creates a Paths object from a list of path strings.
 *
 * This function takes a list of path strings and converts them into a Paths object
 * that can be used in OpenAPI documentation. Each path is added as a PathItem with
 * a GET operation.
 *
 * @param list List of path strings to convert
 * @return Paths object containing all the paths from the list
 */
internal fun getPaths(list: List<String>): Paths {
    val paths = Paths()
    list.forEach { paths.addPathItem(it, PathItem().get(Operation())) }
    return paths
}
