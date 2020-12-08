package io.opengood.autoconfig.openapidocs

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths

class FunctionsTest : FunSpec({

    test("getPaths returns path objects from specified list") {
        val expected = Paths()
            .addPathItem("/foo", PathItem().get(Operation()))
            .addPathItem("/bar", PathItem().get(Operation()))

        getPaths(listOf("/foo", "/bar")) shouldBe expected
    }
})