plugins {
    dokka
    publish
}

configuredKotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":telegram-bot"))
            implementation(libs.env)
            implementation(libs.ktor.client.core)
        }
    }
}

libraryData {
    name = "KtGram Config Env"
    description = "ConfigLoader implementation which works with Environment."
}
