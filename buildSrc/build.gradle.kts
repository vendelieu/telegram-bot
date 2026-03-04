import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

private val kotlinVersion = "2.3"
private val kVer = KotlinVersion.fromVersion(kotlinVersion)

plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        apiVersion.set(kVer)
        languageVersion.set(kVer)
        freeCompilerArgs.add("-Xuse-fir-lt=false")
    }
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
