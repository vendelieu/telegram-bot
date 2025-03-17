plugins {
    kotlin("jvm")
    alias(libs.plugins.ktlinter)
    alias(libs.plugins.ksp)
    publish
    dokka
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.kotlin.embeddable)

    ksp(libs.autoService.ksp)
    implementation(libs.autoService.annotations)

    testImplementation(project(":telegram-bot"))
    testImplementation(libs.test.compile.core)
    testImplementation(libs.test.kotest.junit5)
    testImplementation(libs.test.kotest.assertions)
}

tasks {
    withType<Test> { useJUnitPlatform() }
}

libraryData {
    name = "Aide"
    description = "Compiler plugin for Telegram-bot to help with coding."
}
