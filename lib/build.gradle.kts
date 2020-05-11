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