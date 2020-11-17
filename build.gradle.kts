import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-library")
    id("io.opengood.gradle.config") version "1.1.0"
}

group = "io.opengood.autoconfig"

opengood {
    repoName = "openapi-docs-autoconfig"
}

dependencies {
    implementation("javax.servlet:javax.servlet-api")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:1.4.6")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }
    testImplementation("io.mockk:mockk:1.10.0")
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}
