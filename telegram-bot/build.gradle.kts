import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
}

kotlin {
    jvm()
    js { nodejs() }
//    mingwX64()
//    linuxArm64()
//    linuxX64()
    jvmToolchain(11)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.reflect)

                implementation(libs.ktor.client.core)
                implementation(libs.logging)

                api(libs.coroutines.core)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.logback.classic)
                implementation(libs.test.kotest.junit5)
                implementation(libs.test.kotest.assertions)
                implementation(libs.test.junit.params)
                implementation(libs.test.ktor.mock)
                implementation(libs.mockk)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                allWarningsAsErrors.set(true)
            }
        }
    }
}

group = "eu.vendeli"
version = providers.gradleProperty("libVersion").getOrElse("dev")
description = "Telegram Bot API wrapper, with handy Kotlin DSL."

apply(from = "../publishing.gradle.kts")

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
//    compileKotlin {
//        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
//        kotlinOptions.allWarningsAsErrors = true
//        incremental = true
//    }
//
//    compileTestKotlin {
//        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
//    }
//
//    test {
//        useJUnitPlatform()
//    }

    dokkaHtml.configure {
        outputDirectory.set(layout.buildDirectory.asFile.orNull?.resolve("dokka"))
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
        xml {
            filters {
                excludes {
                    packages(
                        "eu.vendeli.tgbot.interfaces",
                        "eu.vendeli.tgbot.types",
                        "eu.vendeli.tgbot.utils",
                    )
                    classes(
                        "eu.vendeli.tgbot.api.botactions.Close*", // test is ignored
                        "eu.vendeli.tgbot.api.botactions.Logout*",
                        "eu.vendeli.tgbot.api.stickerset.*CustomEmoji*",
                        "eu.vendeli.tgbot.implementations.EnvConfigLoader*",
                    )
                }
            }
        }
    }
}

