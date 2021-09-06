import io.opengood.gradle.enumeration.ProjectType

plugins {
    id("io.opengood.gradle.config")
}

group = "io.opengood.autoconfig"

opengood {
    main {
        projectType = ProjectType.LIB
    }
    artifact {
        description = "Spring Boot auto-configuration for OpenAPI documentation using Spring Doc"
    }
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:_")
    implementation("org.springframework.boot:spring-boot-starter-web:_")
    api("org.springdoc:springdoc-openapi-ui:_")
}
