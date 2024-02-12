import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDate

private val javaTargetVer = JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    id("publish")
}

kotlin {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = javaTargetVer.majorVersion
        }
    }
    js { nodejs() }
    mingwX64()
    linuxArm64()
    linuxX64()
    jvmToolchain(javaTargetVer.majorVersion.toInt())

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.reflect)

                implementation(libs.stately)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.logging)

                api(libs.coroutines.core)
            }
        }
        val jvmTest by getting {
            dependencies {
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
        val linuxArm64Main by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        val linuxX64Main by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        val mingwX64Main by getting {
            dependencies {
                implementation(libs.ktor.client.winhttp)
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
libraryData {
    name.set("Telegram Bot")
    description.set("Telegram Bot API wrapper, with handy Kotlin DSL.")
    version = providers.gradleProperty("libVersion").getOrElse("dev")
}

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
    withType<KotlinCompile> {
        doLast {
            exclude("**/ActivitiesData.kt")
        }
    }

    dokkaHtml.configure {
        outputDirectory.set(layout.buildDirectory.asFile.orNull?.resolve("dokka"))
        dokkaSourceSets {
            collectionSchema.elements.forEach {
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

