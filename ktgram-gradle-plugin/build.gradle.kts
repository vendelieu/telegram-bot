plugins {
    kotlin("jvm")
    `kotlin-dsl`
    `java-gradle-plugin`
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.gradle.publish)
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
    filesMatching("ktgram.properties") {
        expand("ktgramVer" to projectVersion)
    }
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.plugin)
}
