import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.dokka")
}

val springFoxSwaggerVersion = "3.0.0"
val swaggerCoreVersion = "2.1.3"

dependencies {
    implementation("javax.servlet:javax.servlet-api")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("io.springfox:springfox-swagger2:${springFoxSwaggerVersion}")
    implementation("io.springfox:springfox-swagger-ui:${springFoxSwaggerVersion}")
    implementation("io.swagger.core.v3:swagger-annotations:${swaggerCoreVersion}")
    implementation("io.swagger.core.v3:swagger-core:${swaggerCoreVersion}")
    implementation("io.swagger.core.v3:swagger-models:${swaggerCoreVersion}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val docsJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs JAR"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            artifact(sourcesJar)
            artifact(docsJar)
        }
    }

    repositories {
        maven {
            url = uri("$buildDir/repository")
        }
    }
}