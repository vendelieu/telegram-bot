plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.publisher) apply false
}

dependencies {
    implementation(libs.kotlin.serialization)
    compileOnly(libs.publisher)
}

repositories {
    mavenCentral()
}
