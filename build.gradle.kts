plugins {
    id("io.opengood.gradle.config")
}

group = "io.opengood.autoconfig"

opengood {
    artifact {
        description = "Spring Boot auto-configuration for OpenAPI documentation using Spring Doc"
    }
}

dependencies {
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:_")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
