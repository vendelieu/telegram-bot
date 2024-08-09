plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.serialization)
    implementation("tech.yanand.gradle:maven-central-publish:1.1.1")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
