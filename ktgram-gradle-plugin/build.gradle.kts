plugins {
    kotlin("jvm")
    alias(libs.plugins.gradle.publish)
    `kotlin-dsl`
    `java-gradle-plugin`
    id("publish")
}

val publicationName = "Telegram-bot Gradle Plugin"
val publicationDescription = "A plugin for customizing and adding Telegram-bot library."

gradlePlugin {
    website = "https://vendeli.eu"
    vcsUrl = "https://github.com/vendelieu/telegram-bot"
    isAutomatedPublishing = true

    plugins.register("telegram-bot") {
        id = "eu.vendeli.telegram-bot"
        displayName = publicationName
        description = publicationDescription
        @Suppress("UnstableApiUsage")
        tags = listOf("kotlin", "telegram", "bot", "spring-boot", "ktor", "multiplatform")
        implementationClass = "eu.vendeli.ktgram.gradle.KtGramPlugin"
    }
}

libraryData {
    name = publicationName
    description = publicationDescription
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
