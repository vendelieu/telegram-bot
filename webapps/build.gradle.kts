plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.js.plain.objects)
    publish
    dokka
}

libraryData {
    name = "WebApps"
    description = "Implementation of WebApp applications Telegram Bot Api"
}

configuredKotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization)
            implementation(project(":telegram-bot"))
        }
    }
}
