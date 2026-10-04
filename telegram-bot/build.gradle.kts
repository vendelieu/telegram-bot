plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compatability.validator)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.test.retry)
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
        }
        jvmTest.dependencies {
            implementation(libs.test.dotenv.kotlin)
            implementation(libs.test.kotest.junit5)
            implementation(libs.test.kotest.assertions)
            implementation(libs.test.kotest.property)
            implementation(libs.test.coroutines)
            implementation(libs.test.ktor.client.mock)
            implementation(libs.logback)
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
        linuxX64Main.dependencies {
            implementation(libs.ktor.client.curl)
        }
        linuxArm64Main.dependencies {
            implementation(libs.ktor.client.curl)
        }
        mingwX64Main.dependencies {
            implementation(libs.ktor.client.winhttp)
        }
        macosArm64Main.dependencies {
            implementation(libs.ktor.client.darwin)
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
    add("kspCommonMainMetadata", project(":api-sentinel"))
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
    withType<Test> {
        useJUnitPlatform()
        // Retry once on CI so that a flaky test is reported as flaky instead of failing the whole build.
        retry {
            maxRetries.set(System.getenv("TEST_RETRIES")?.toIntOrNull() ?: if (System.getenv("CI") != null) 1 else 0)
            maxFailures.set(10)
            failOnPassedAfterRetry.set(false)
        }
    }
    // Re-records Telegram responses into src/jvmTest/resources/tg-fixtures (needs real credentials in .env).
    register<Test>("recordFixtures") {
        group = "verification"
        description = "Runs the jvm tests against live Telegram and records the responses as replay fixtures."
        val jvmTest = named<Test>("jvmTest").get()
        testClassesDirs = jvmTest.testClassesDirs
        classpath = jvmTest.classpath
        environment("TG_TEST_MODE", "record")
        systemProperty("tg.fixtures.dir", layout.projectDirectory.dir("src/jvmTest/resources/tg-fixtures").asFile.absolutePath)
        outputs.upToDateWhen { false }
    }
    // Runs the same tests against live Telegram without touching fixtures.
    register<Test>("liveTest") {
        group = "verification"
        description = "Runs the jvm tests against live Telegram (no replay)."
        val jvmTest = named<Test>("jvmTest").get()
        testClassesDirs = jvmTest.testClassesDirs
        classpath = jvmTest.classpath
        environment("TG_TEST_MODE", "live")
        outputs.upToDateWhen { false }
    }
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
