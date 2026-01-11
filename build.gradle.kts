allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "eu.vendeli"
    version = providers.gradleProperty("libVersion").getOrElse("dev")
}

plugins {
    alias(libs.plugins.ktlinter) apply false
    alias(libs.plugins.deteKT) apply false
}

tasks.register("prepareRelease") {
    dependsOn("telegram-bot:clean")
    dependsOn("telegram-bot:kspCommonMainKotlinMetadata")
    dependsOn("telegram-bot:apiCheck")

    dependsOn("telegram-bot:kdocUpdate")
    dependsOn("telegram-bot:formatKotlin")
    dependsOn("ktgram-gradle-plugin:formatKotlin")
    dependsOn("api-sentinel:formatKotlin")

    dependsOn("ktnip:detekt")
    dependsOn("telegram-bot:detekt")
}
