plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.dokka)
    implementation(libs.publisher)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
