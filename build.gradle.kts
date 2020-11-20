import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("base")
    id("java-library")
    id("signing")
    id("io.opengood.gradle.config") version "1.0.0"
}

group = "io.opengood.autoconfig"
base.archivesBaseName = "openapi-docs-autoconfig"

object Versions {
    const val SPRING_DOC_OPENAPI = "1.5.0"
}

dependencies {
    implementation("javax.servlet:javax.servlet-api")
    implementation("org.springframework.boot:spring-boot-starter-web")
    api("org.springdoc:springdoc-openapi-ui:${Versions.SPRING_DOC_OPENAPI}")
}

tasks.getByName<Jar>("jar") {
    enabled = true

    into("META-INF/maven/${project.group}/${project.name}") {
        dependsOn.add("generatePomFileForMavenJavaPublication")
        from(tasks.getByName("generatePomFileForMavenJavaPublication"))
        rename(".*", "pom.xml")
    }
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks {
    val sourcesJar by creating(Jar::class) {
        dependsOn.add("classes")
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        from(javadoc)
        archiveClassifier.set("javadoc")
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
    }
}

tasks.getByName<Upload>("uploadArchives") {
    val ossrhUsername: String by project
    val ossrhPassword: String by project

    repositories {
        withConvention(MavenRepositoryHandlerConvention::class) {
            mavenDeployer {
                beforeDeployment {
                    signing.signPom(this)
                }

                withGroovyBuilder {
                    "repository"("url" to "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
                        "authentication"(
                            "userName" to ossrhUsername,
                            "password" to ossrhPassword
                        )
                    }
                    "snapshotRepository"("url" to "https://oss.sonatype.org/content/repositories/snapshots/") {
                        "authentication"(
                            "userName" to ossrhUsername,
                            "password" to ossrhPassword
                        )
                    }
                }

                pom.project {
                    withGroovyBuilder {
                        "name"("OpenAPI Docs Auto Configuration")
                        "packaging"("jar")
                        "description"("Spring Boot auto-configuration for OpenAPI documentation using Spring Doc")
                        "url"("https://github.com/opengoodio/openapi-docs-autoconfig")
                        "scm" {
                            "connection"("scm:git:https://github.com/opengoodio/openapi-docs-autoconfig.git")
                            "developerConnection"("scm:git:https://github.com/opengoodio/openapi-docs-autoconfig.git")
                            "url"("https://github.com/opengoodio/openapi-docs-autoconfig")
                        }
                        "licenses" {
                            "license" {
                                "name"("MIT License")
                                "url"("https://github.com/opengoodio/openapi-docs-autoconfig/blob/main/LICENSE")
                            }
                        }
                        "developers" {
                            "developer" {
                                "id"("opengood")
                                "name"("OpenGood")
                                "email"("dev@opengood.io")
                            }
                        }
                    }
                }
            }
        }
    }
}

afterEvaluate {
    val publishing = extensions.findByType(PublishingExtension::class.java) ?: return@afterEvaluate
    afterEvaluate {
        publishing.apply {
            publications {
                val mavenJava by registering(MavenPublication::class) {
                    from(components["java"])
                }
            }
        }
    }
}

signing {
    sign(configurations.archives.get())
}
