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
    implementation("org.springdoc:springdoc-openapi-ui:${Versions.SPRING_DOC_OPENAPI}")
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
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

signing {
    sign(configurations.archives.get())
}
