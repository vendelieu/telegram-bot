plugins {
    kotlin("jvm")
    `kotlin-dsl`
    `java-gradle-plugin`
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.gradle.publish)
    dokka
}

gradlePlugin {
    website = "https://vendeli.eu"
    vcsUrl = "https://github.com/vendelieu/telegram-bot"

    plugins.register("telegram-bot") {
        id = "eu.vendeli.telegram-bot"
        displayName = "Telegram-bot Gradle Plugin"
        description = "A plugin for customizing and adding Telegram-bot library."
        tags = listOf("kotlin", "telegram", "bot", "spring-boot", "ktor", "multiplatform")
        implementationClass = "eu.vendeli.ktgram.gradle.KtGramPlugin"
    }
}

tasks.processResources {
    val projectVersion = project.version
    inputs.property("version", projectVersion)

    val ktorVersion = libs.versions.ktor.get()
    inputs.property("ktor", ktorVersion)

    val logbackVersion = libs.versions.logback.get()
    inputs.property("logback", logbackVersion)

    filesMatching("ktgram.properties") {
        expand(
            "ktgramVer" to projectVersion,
            "ktorVer" to ktorVersion,
            "logbackVer" to logbackVersion,
        )
    }
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.plugin)
}
