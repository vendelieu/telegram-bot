allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "eu.vendeli"
    version = providers.gradleProperty("libVersion").getOrElse("dev")
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ktlinter) apply false
    alias(libs.plugins.deteKT) apply false
}

tasks.register<ScGenerator>("generateBotSc")

tasks.create("prepareRelease") {
    dependsOn("ksp:formatKotlin")
    dependsOn("telegram-bot:formatKotlin")

    dependsOn("ksp:detekt")
    dependsOn("telegram-bot:detekt")

    dependsOn("telegram-bot:apiCheck")
}
