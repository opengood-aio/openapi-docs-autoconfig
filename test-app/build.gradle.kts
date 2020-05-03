plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

application {
    mainClassName = "io.opengood.autoconfig.swagger.app.SwaggerTestApplication"
}

dependencies {
    implementation(project(":lib"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}