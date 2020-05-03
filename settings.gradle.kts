rootProject.name = "swagger-auto-configuration"

include(":lib")
project(":lib").projectDir = file("lib")

include(":test-app")
project(":test-app").projectDir = file("test-app")
