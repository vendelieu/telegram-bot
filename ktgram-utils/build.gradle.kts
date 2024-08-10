plugins {
    id("publish")
}

configuredKotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":telegram-bot"))
                implementation(libs.kotlin.datetime)
            }
        }
    }
}

libraryData {
    name.set("KtGram utils")
    description.set("KtGram library utilities.")
}
