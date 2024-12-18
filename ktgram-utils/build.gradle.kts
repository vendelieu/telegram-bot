plugins {
    dokka
    publish
}

configuredKotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":telegram-bot"))
            implementation(libs.urlencoder)
            implementation(libs.krypto)
        }
    }
}

libraryData {
    name = "KtGram utils"
    description = "KtGram library utilities."
}
