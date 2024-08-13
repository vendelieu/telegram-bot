allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "eu.vendeli"
    version = providers.gradleProperty("libVersion").getOrElse("dev-SNAPSHOT")
}

plugins {
    alias(libs.plugins.ktlinter) apply false
    alias(libs.plugins.deteKT) apply false
}

tasks.create("prepareRelease") {
    dependsOn("ksp:formatKotlin")
    dependsOn("telegram-bot:formatKotlin")

    dependsOn("ksp:detekt")
    dependsOn("telegram-bot:detekt")

    dependsOn("telegram-bot:apiCheck")
}
