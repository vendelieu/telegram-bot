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
    name = "KtGram utils"
    description = "KtGram library utilities."
}
