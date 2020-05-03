plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("javax.servlet:javax.servlet-api")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("io.swagger.core.v3:swagger-annotations:2.1.2")
    implementation("io.swagger.core.v3:swagger-core:2.1.2")
    implementation("io.swagger.core.v3:swagger-models:2.1.2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}