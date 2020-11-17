pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "opengood"
            url = uri("https://maven.pkg.github.com/opengoodio/config-gradle-plugin")
            credentials {
                val properties = java.util.Properties()
                properties.load(java.io.FileInputStream("${System.getenv("HOME")}/.github.properties"))

                username = properties["gh.api.user"] as String
                password = properties["gh.api.key"] as String
            }
        }
    }
}

rootProject.name = "openapi-docs-autoconfig"
