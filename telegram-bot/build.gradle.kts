import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compatability.validator)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ksp)
    id("publish")
}

configuredKotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization)
            implementation(libs.kotlin.reflect)

            implementation(libs.stately)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)

            api(libs.coroutines.core)
            api(libs.kotlin.datetime)
        }
        jvmTest.dependencies {
            implementation(libs.test.kotest.junit5)
            implementation(libs.test.kotest.assertions)
            implementation(libs.mockk)
        }
        jvmMain.dependencies {
            implementation(libs.logback)
            implementation(libs.ktor.client.java)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        named("linuxX64Main").dependencies {
            implementation(libs.ktor.client.curl)
        }
        named("mingwX64Main").dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.allWarningsAsErrors = true
            }
        }
    }
}

libraryData {
    name = "Telegram Bot"
    description = "Telegram Bot API wrapper, with handy Kotlin DSL."
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

dependencies {
    add("kspCommonMainMetadata", project(":helper"))
}

ksp {
    arg(
        "utilsDir",
        rootDir.resolve("ktgram-utils/src/commonMain/kotlin/").absolutePath,
    )
    arg(
        "tgBaseDir",
        rootDir.resolve("telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot").absolutePath,
    )
    arg(
        "apiFile",
        rootDir.resolve("buildSrc/src/main/resources/api.json").absolutePath,
    )
}

tasks {
    register<Kdokker>("kdocUpdate")
    withType<Test> { useJUnitPlatform() }
    named("build") { dependsOn("kspCommonMainKotlinMetadata") }
    dokkaHtml.configure {
        outputDirectory = layout.buildDirectory.asFile.orNull
            ?.resolve("dokka")
        dokkaSourceSets {
            named("commonMain") { sourceRoots.setFrom(project.projectDir.resolve("src/commonMain/kotlin")) }
            named("jvmMain") { sourceRoots.setFrom(project.projectDir.resolve("src/jvmMain/kotlin")) }
            named("jsMain") { sourceRoots.setFrom(project.projectDir.resolve("src/jsMain/kotlin")) }
            if ("nativeMain" in names) named("nativeMain") {
                sourceRoots.setFrom(project.projectDir.resolve("src/nativeMain/kotlin"))
            }
            collectionSchema.elements.forEach { _ -> moduleName = "Telegram Bot" }
        }
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customAssets = listOf(rootDir.resolve("assets/logo-icon.svg"))
            footerMessage = "© ${LocalDate.now().year} Vendelieu"
            homepageLink = "https://github.com/vendelieu/telegram-bot"
        }
    }
}

apiValidation {
    @Suppress("OPT_IN_USAGE")
    klib.enabled = true
    ignoredPackages.add("utils")
    nonPublicMarkers.apply {
        add("eu.vendeli.tgbot.annotations.internal.ExperimentalFeature")
        add("eu.vendeli.tgbot.annotations.internal.KtGramInternal")
    }
}

kover.reports.filters.excludes {
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
