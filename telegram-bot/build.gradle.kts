plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compatability.validator)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    publish
    dokka
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
            implementation(libs.ktor.client.java)
            implementation(libs.slf4j.api)
            compileOnly(libs.logback)
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
    )
}
