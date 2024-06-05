plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.js.plain.objects)
    id("publish")
}

libraryData {
    name = "WebApps"
    description = "Implementation of WebApp applications Telegram Bot Api"
}

kotlin {
    js { browser() }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.datetime)
                implementation(project(":telegram-bot"))
            }
        }
    }
}
