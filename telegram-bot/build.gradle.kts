import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
}

val javaTargetVersion = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.databind)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)

    implementation(libs.mu.logging)
    api(libs.logback.classic)

    implementation(libs.reflections)
    implementation(libs.kotlin.reflections)

    api(libs.coroutines.core)

    testImplementation(libs.logback.classic)
    testImplementation(libs.test.kotest.junit5)
    testImplementation(libs.test.kotest.assertions)
    testImplementation(libs.test.junit.params)
    testImplementation(libs.test.ktor.mock)
}

group = "eu.vendeli"
version = "2.9.0"

apply(from = "publishing.gradle.kts")

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}

buildscript {
    dependencies {
        classpath(libs.dokka.base)
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
        incremental = true
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
    }

    test {
        useJUnitPlatform()
    }

    dokkaHtml.configure {
        outputDirectory.set(buildDir.resolve("dokka"))
        dokkaSourceSets {
            named("main") {
                moduleName.set("Telegram Bot")
            }
        }
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customStyleSheets = listOf(rootDir.resolve("assets/logo-styles.css"))
            customAssets = listOf(rootDir.resolve("assets/tgbotkt-logo.png"))
            footerMessage = "© ${LocalDate.now().year} Vendelieu"
        }
    }
}

koverReport {
    defaults {
        log {
            coverageUnits = MetricType.LINE
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = javaTargetVersion
    targetCompatibility = javaTargetVersion
}
