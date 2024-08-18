plugins {
    kotlin("jvm")
    alias(libs.plugins.gradle.publish)
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    website = "https://vendeli.eu"
    vcsUrl = "https://github.com/vendelieu/telegram-bot"
    isAutomatedPublishing = true

    plugins.register("telegram-bot") {
        id = "eu.vendeli.telegram-bot"
        displayName = "Telegram-bot Gradle Plugin"
        description = "A plugin for customizing and adding Telegram-bot library."
        @Suppress("UnstableApiUsage")
        tags = listOf("kotlin", "telegram", "bot", "spring-boot", "ktor", "multiplatform")
        implementationClass = "eu.vendeli.ktgram.gradle.KtGramPlugin"
    }
}

tasks.processResources {
    val projectVersion = project.version
    inputs.property("version", projectVersion)
    filesMatching("ktgram.properties") {
        expand("ktgramVer" to projectVersion)
    }
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.plugin)
}
