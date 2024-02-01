val javaTargetVersion = JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.deteKT)
}

group = "eu.vendeli"
version = providers.gradleProperty("libVersion").getOrElse("dev")
description = "KSP plugin for Telegram-bot lib to collect actions."

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

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
        kotlinOptions.allWarningsAsErrors = true
        incremental = true
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = javaTargetVersion.majorVersion
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = javaTargetVersion
    targetCompatibility = javaTargetVersion
}
