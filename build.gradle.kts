allprojects {
    repositories {
        mavenCentral()
    }
    group = "eu.vendeli"
    version = providers.gradleProperty("libVersion").getOrElse("dev")
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ktlinter) apply false
    alias(libs.plugins.deteKT) apply false
}
