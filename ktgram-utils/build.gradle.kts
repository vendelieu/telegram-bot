plugins {
    id("publish")
}

configuredKotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":telegram-bot"))
            }
        }
    }
}

libraryData {
    name.set("KtGram utils")
    description.set("KtGram library utilities.")
}
