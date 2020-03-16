import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object DependencyVersions {
    const val mockK = "1.9.3"
    const val springFoxSwagger = "2.9.2"
    const val swagger = "2.0.10"
}

plugins {
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

group = "io.opengood.autoconfig"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("javax.servlet:javax.servlet-api")
    implementation("io.springfox:springfox-swagger2:${DependencyVersions.springFoxSwagger}")
    implementation("io.springfox:springfox-swagger-ui:${DependencyVersions.springFoxSwagger}")
    implementation("io.swagger.core.v3:swagger-annotations:${DependencyVersions.swagger}")
    implementation("io.swagger.core.v3:swagger-core:${DependencyVersions.swagger}")
    implementation("io.swagger.core.v3:swagger-models:${DependencyVersions.swagger}")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.mockk:mockk:${DependencyVersions.mockK}")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED, STANDARD_ERROR)
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }

    maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 + 1

    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")

    doFirst {
        println("***************************************************")
        println(" >> Running Tests")
        println("***************************************************")
    }

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) {
                val output =
                    "Results: ${result.resultType} " +
                        "(" +
                        "${result.testCount} tests, " +
                        "${result.successfulTestCount} successes, " +
                        "${result.failedTestCount} failures, " +
                        "${result.skippedTestCount} skipped" +
                        ")"
                val startItem = "| "
                val endItem = " |"
                val repeatLength = startItem.length + output.length + endItem.length
                println("""
                    |
                    |${"-".repeat(repeatLength)}
                    |$startItem$output$endItem
                    |${"-".repeat(repeatLength)}
                    |
                    """.trimMargin())
            }
        }
    })

    doLast {
        println("***************************************************")
        println(" >> Tests FINISHED")
        println("***************************************************")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
