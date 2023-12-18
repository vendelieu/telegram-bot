plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
}

group = "eu.vendeli"
version = providers.gradleProperty("libVersion").getOrElse("dev")

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ksp)
    implementation(libs.poet)
    implementation(libs.poet.ksp)
    implementation(project(":telegram-bot"))
}

apply(from = "../publishing.gradle.kts")

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.from(files("$rootDir/detekt.yml"))
}

