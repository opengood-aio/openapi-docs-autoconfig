import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.ben-manes.versions") version "0.28.0"
    id("org.jetbrains.dokka") version "0.10.1"
}

allprojects {
    apply(plugin = "java")

    group = "io.opengood.autoconfig"
    java.sourceCompatibility = JavaVersion.VERSION_11

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
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

}

subprojects {
    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation("org.springframework.boot:spring-boot-starter")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        testImplementation("io.mockk:mockk:1.10.0")
    }
}